package com.varsql.core.db.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.meta.handler.DBMetaHandler;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ObjectColumnInfo;
import com.varsql.core.db.valueobject.ObjectInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.sql.ConstraintType;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.utils.StringUtils;


/**
 *
 * @FileName : DBMetaDataUtil.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명: db meta 정보 가져오기.
 * @변경이력	:
 */
public final class DBMetaDataUtil {

	private final Logger logger = LoggerFactory.getLogger(DBMetaDataUtil.class);

	protected List<TableInfo> tableInfo(DatabaseParamInfo dataParamInfo, Connection conn, String type) throws SQLException{
		List<TableInfo>  reLst = new ArrayList<TableInfo>();
		ResultSet rs = null;
		try {
			DatabaseMetaData dbmd = conn.getMetaData();

			String[] types = { type };
			rs = dbmd.getTables(null, dataParamInfo.getSchema(), "%", types);

			TableInfo tableInfo = null;
			while (rs.next()) {
				tableInfo= new TableInfo();
				tableInfo.setName(rs.getString(MetaColumnConstants.TABLE_NAME));
				tableInfo.setRemarks(StringUtils.nullToString(rs.getString(MetaColumnConstants.REMARKS)));

				reLst.add(tableInfo);
			}
		}finally{
			JdbcUtils.close(rs);
		}

		return reLst;
	}

	protected List<TableInfo> tableAndColumnsInfo(DatabaseParamInfo dataParamInfo, Connection conn, MetaControlBean dbInstanceFactory, String type,List<TableInfo> tableList) throws SQLException{
		List<TableInfo> reLst = new ArrayList<TableInfo>();

		ResultSet colRs = null;
		String schema = dataParamInfo.getSchema();
		try {

			DatabaseMetaData dbmd = conn.getMetaData();

			DataTypeFactory dataTypeFactory = dbInstanceFactory.getDataTypeImpl();

			List<ColumnInfo> columnList = null;
			String tableNm = "";

			Map<String , HashSet<String>> pkMap = new HashMap<String , HashSet<String>>();
			Map<String , TableInfo> tableInfoMap = new HashMap<String , TableInfo>();

			for(TableInfo tableInfo: tableList){
				columnList = new ArrayList<ColumnInfo>();

				tableNm = tableInfo.getName();

				colRs = dbmd.getPrimaryKeys(null, schema, tableNm);

				HashSet<String> keyColumn = new HashSet<String>();
				while(colRs.next()){
					keyColumn.add(colRs.getString(MetaColumnConstants.COLUMN_NAME));
				}
				colRs.close();

				pkMap.put(tableNm, keyColumn);

				tableInfoMap.put(tableNm, tableInfo);

				tableInfo.setColList(columnList);

				reLst.add(tableInfo);
			}

			colRs = dbmd.getColumns(null, schema, null, null);

			while(colRs.next()){
				tableNm = colRs.getString(MetaColumnConstants.TABLE_NAME);

				if(tableInfoMap.containsKey(tableNm)) {
					Set keyColumn = pkMap.get(tableNm);
					
					String cName=  colRs.getString(MetaColumnConstants.COLUMN_NAME);
					String typeName = colRs.getString(MetaColumnConstants.TYPE_NAME);

					int degitsLen = colRs.getInt(MetaColumnConstants.DECIMAL_DIGITS);
					int columnSize = colRs.getInt(MetaColumnConstants.COLUMN_SIZE);
					
					DataType dataTypeInfo = dataTypeFactory.getDataType(typeName);

					ColumnInfo column = new ColumnInfo(); 
					column.setName(cName);
					column.setTypeCode(dataTypeInfo.getTypeCode());
					column.setTypeName(typeName);
					column.setLength(columnSize);
					column.setDefaultVal(StringUtils.nullToString(colRs.getString(MetaColumnConstants.COLUMN_DEF)));
					column.setNullable(StringUtils.nullToString(colRs.getString(MetaColumnConstants.IS_NULLABLE)));
					column.setAutoincrement("");
					column.setTypeName(dataTypeInfo.getTypeName());
					column.setTypeAndLength(dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(typeName, dataTypeInfo, null, columnSize, columnSize, degitsLen));

					if(keyColumn !=null){
						column.setConstraints(keyColumn.contains(cName)?ConstraintType.PRIMARY.getType():"");
					}
					
					tableInfoMap.get(tableNm).getColList().add(column);
				}
			}
			colRs.close();
		}finally{
			JdbcUtils.close(colRs);
		}
		return reLst;
	}

	protected List<ObjectInfo> proceduresInfo(DatabaseParamInfo dataParamInfo, Connection conn) throws SQLException {

		List<ObjectInfo>  reLst = new ArrayList<ObjectInfo>();
		ResultSet rs = null;
		try {
			DatabaseMetaData dbmd = conn.getMetaData();

			rs = dbmd.getProcedures(null, dataParamInfo.getSchema(), null);

			ObjectInfo oi = null;
			while(rs.next()){
				oi =  new ObjectInfo();

				oi.setName(StringUtils.nullToString(rs.getString(MetaColumnConstants.PROCEDURE_NAME)));
				oi.setRemarks(StringUtils.nullToString(rs.getString(MetaColumnConstants.PROCEDURE_NAME)));
				oi.setType(StringUtils.nullToString(rs.getString(MetaColumnConstants.PROCEDURE_TYPE)));
				reLst.add(oi);
			}
		}finally{
			JdbcUtils.close(rs);
		}
		return reLst;
	}

	public List<ObjectInfo> proceduresAndMetadatas(DatabaseParamInfo dataParamInfo, Connection conn,
			MetaControlBean dbInstanceFactory, List<ObjectInfo> objectInfoList) throws SQLException {
		List<ObjectInfo> reLst = new ArrayList<ObjectInfo>();

		ResultSet colRs = null;
		String schema = dataParamInfo.getSchema();
		try {
			DatabaseMetaData dbmd = conn.getMetaData();

			List<ObjectColumnInfo> columnList = null;
			String objNm = "";

			ObjectColumnInfo oci = null;

			for(ObjectInfo objectInfo: objectInfoList){
				columnList = new ArrayList<ObjectColumnInfo>();

				objNm = objectInfo.getName();

				colRs = dbmd.getProcedureColumns(null, schema, objNm, null);

				String cName= "";
				while(colRs.next()){
					oci =new ObjectColumnInfo();
					cName=  colRs.getString(MetaColumnConstants.COLUMN_NAME);

					oci.setName(cName);
					oci.setTypeName(colRs.getString(MetaColumnConstants.TYPE_NAME));
					oci.setComment(StringUtils.nullToString(colRs.getString(MetaColumnConstants.REMARKS)));
					oci.setColumnType(colRs.getString(MetaColumnConstants.PROCEDURE_COL_TYPE));

					columnList.add(oci);
				}
				objectInfo.setColList(columnList);
				reLst.add(objectInfo);
				colRs.close();
			}
		}finally{
			JdbcUtils.close(colRs);
		}
		return reLst;
	}

	public List<ObjectInfo> functionAndMetadatas(DatabaseParamInfo dataParamInfo, Connection conn, MetaControlBean dbInstanceFactory, List<ObjectInfo> objectInfoList) throws SQLException {
		List<ObjectInfo> reLst = new ArrayList<ObjectInfo>();

		ResultSet colRs = null;
		String schema = dataParamInfo.getSchema();
		try {
			DatabaseMetaData dbmd = conn.getMetaData();

			List<ObjectColumnInfo> columnList = null;
			String objNm = "";

			ObjectColumnInfo oci = null;

			for(ObjectInfo objectInfo: objectInfoList){
				columnList = new ArrayList<ObjectColumnInfo>();

				objNm = objectInfo.getName();

				colRs = dbmd.getFunctionColumns(null, schema, objNm, null);

				String cName= "";
				while(colRs.next()){
					oci =new ObjectColumnInfo();
					cName=  colRs.getString(MetaColumnConstants.COLUMN_NAME);

					oci.setName(cName);
					oci.setTypeName(colRs.getString(MetaColumnConstants.TYPE_NAME));
					oci.setComment(StringUtils.nullToString(colRs.getString(MetaColumnConstants.REMARKS)));
					oci.setColumnType(colRs.getString(MetaColumnConstants.PROCEDURE_COL_TYPE));

					columnList.add(oci);
				}
				objectInfo.setColList(columnList);
				reLst.add(objectInfo);

				colRs.close();
			}
		}finally{
			JdbcUtils.close(colRs);
		}
		return reLst;
	}

	public List<ObjectInfo> functionInfo(DatabaseParamInfo dataParamInfo, Connection conn) throws SQLException {

		List<ObjectInfo>  reLst = new ArrayList<ObjectInfo>();
		ResultSet rs = null;
		try {
			DatabaseMetaData dbmd = conn.getMetaData();

			rs = dbmd.getFunctions(null, dataParamInfo.getSchema(), null);

			ObjectInfo oi = null;
			while(rs.next()){
				oi =  new ObjectInfo();
				oi.setName(StringUtils.nullToString(rs.getString(MetaColumnConstants.FUNCTION_NAME)));
				oi.setRemarks(StringUtils.nullToString(rs.getString(MetaColumnConstants.REMARKS)));
				oi.setType(StringUtils.nullToString(rs.getString(MetaColumnConstants.FUNCTION_TYPE)));
				reLst.add(oi);
			}
		}finally{
			JdbcUtils.close(rs);
		}
		return reLst;
	}
}
