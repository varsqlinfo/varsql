package com.varsql.core.db.mybatis.handler.resultset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.StringUtils;

public class TableInfoHandler implements ResultHandler<DataMap> {
	private List<TableInfo> tableInfoList;

	private TableInfo currentTableInfo;
	private String beforeTableName = "";

	private DataTypeFactory dataTypeFactory;
	private Map<String,Integer> tableIndexInfo;
	private boolean useTableIndex;
	private List<String> tableNameList;

	public TableInfoHandler(DataTypeFactory dataTypeFactory){
		this.dataTypeFactory = dataTypeFactory;
		this.tableInfoList = new ArrayList<TableInfo>();
		this.useTableIndex = false;
	}

	public TableInfoHandler(DataTypeFactory dataTypeFactory, List<TableInfo> tableInfoList){
		this.dataTypeFactory = dataTypeFactory;
		this.tableInfoList = tableInfoList;

		Map<String,Integer> tableIndexInfo  = new HashMap<String,Integer>();
		int idx=-1;

		StringBuilder sb =new StringBuilder();

		List<String> tableNameList = new ArrayList<String>();
		boolean addFlag = false;
		for(TableInfo tblInfo :   tableInfoList){
			tableIndexInfo.put(tblInfo.getName(), ++idx);

			sb.append(addFlag ? ",":"" ).append("'").append(tblInfo.getName()).append("'");

			addFlag = true;
			if(idx !=0 && (idx+1)%1000==0){
				tableNameList.add(sb.toString());
				sb =new StringBuilder();
				addFlag = false;
			}
		}

		if(sb.length() > 0){
			tableNameList.add(sb.toString());
		}

		this.tableNameList = tableNameList;
		this.tableIndexInfo = tableIndexInfo;
		this.useTableIndex = true;
	}

	public List<TableInfo> getTableInfoList() {
		return tableInfoList;
	}

	public List<String> getTableNameList() {
		return tableNameList;
	}

	@Override
	public void handleResult(ResultContext<? extends DataMap> paramResultContext) {
		DataMap rowData = paramResultContext.getResultObject();

		String tblName = rowData.getString(MetaColumnConstants.TABLE_NAME);

		if(!tblName.equals(beforeTableName)){

			if(useTableIndex){
				currentTableInfo = this.tableInfoList.get(this.tableIndexInfo.get(tblName));
				currentTableInfo.setColList(new ArrayList<ColumnInfo>());
				currentTableInfo.setRemarks(StringUtils.nullToString(currentTableInfo.getRemarks(), ""));
			}else{
				currentTableInfo = new TableInfo();

				currentTableInfo.setName(tblName);
				currentTableInfo.setSchema(rowData.getString(MetaColumnConstants.SCHEMA));
				currentTableInfo.setColList(new ArrayList<ColumnInfo>());
				currentTableInfo.setRemarks(rowData.getString(MetaColumnConstants.REMARKS,""));

				tableInfoList.add(currentTableInfo);
			}
		}

		ColumnInfo column = new ColumnInfo();

		String cName=  rowData.getString(MetaColumnConstants.COLUMN_NAME);
		int columnSize= rowData.getInt(MetaColumnConstants.COLUMN_SIZE);
		int dataPrecision= rowData.getInt(MetaColumnConstants.DATA_PRECISION, columnSize);
		int degitsLen= rowData.getInt(MetaColumnConstants.DECIMAL_DIGITS);

		String dataType = rowData.getString(MetaColumnConstants.DATA_TYPE);
		DataType dataTypeInfo = dataTypeFactory.getDataType(dataType);

		column.setName(cName);
		column.setDataType(dataType);
		Object lenInfoObj = rowData.get(MetaColumnConstants.COLUMN_SIZE);

		if(lenInfoObj != null) {
			if(lenInfoObj instanceof Integer) {
				column.setLength(Integer.parseInt(lenInfoObj+""));
			}else{
				column.setLength(new BigDecimal(lenInfoObj+""));
			}
		}
		
		column.setLength(dataTypeInfo.getMetaDataHandler().getColumnLength(columnSize));
		column.setDefaultVal(rowData.getString(MetaColumnConstants.COLUMN_DEF));
		column.setNullable(rowData.getString(MetaColumnConstants.IS_NULLABLE));
		column.setTypeName(dataTypeInfo.getTypeName());

		String typeAndLength =rowData.getString(MetaColumnConstants.TYPE_NAME_SIZE ,"");
		
		column.setTypeAndLength(dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(dataType, dataTypeInfo, typeAndLength, columnSize, dataPrecision, degitsLen));

		column.setComment(rowData.getString(MetaColumnConstants.COMMENT,""));

		column.setConstraints(rowData.getString(MetaColumnConstants.CONSTRAINTS,""));

		currentTableInfo.addColInfo(column);

		beforeTableName = tblName;

	}

}
