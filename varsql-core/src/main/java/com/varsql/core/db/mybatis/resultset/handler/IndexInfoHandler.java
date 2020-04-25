package com.varsql.core.db.mybatis.resultset.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.varsql.core.db.valueobject.IndexInfo;
import com.varsql.core.db.valueobject.ObjectColumnInfo;
import com.vartech.common.app.beans.ParamMap;

public class IndexInfoHandler implements ResultHandler<ParamMap> {
	private List<IndexInfo> indexInfoList = new ArrayList<IndexInfo>();
	
	private IndexInfo currentIndexInfo; 
	private String beforeTableName = ""; 
	
	private DataTypeImpl dataTypeImpl; 
	
	public IndexInfoHandler(DataTypeImpl dataTypeImpl){
		this.dataTypeImpl = dataTypeImpl; 
	}
	
	public List<IndexInfo> getIndexInfoList() {
		return indexInfoList;
	}
	@Override
	public void handleResult(ResultContext<? extends ParamMap> paramResultContext) {
		ParamMap rowData = paramResultContext.getResultObject();
		
		String indexName = rowData.getString(MetaColumnConstants.INDEX_NAME);
		
		if(!indexName.equals(beforeTableName)){
			currentIndexInfo = new IndexInfo();
			
			currentIndexInfo.setName(indexName);
			currentIndexInfo.setBufferPool(rowData.getString(MetaColumnConstants.BUFFER_POOL));
			currentIndexInfo.setStatus(rowData.getString(MetaColumnConstants.STATUS));
			currentIndexInfo.setTableSpace(rowData.getString(MetaColumnConstants.TABLE_SPACE));
			currentIndexInfo.setTblName(rowData.getString(MetaColumnConstants.TABLE_NAME));
			currentIndexInfo.setType(rowData.getString(MetaColumnConstants.INDEX_TYPE));
			currentIndexInfo.setColList(new ArrayList<ObjectColumnInfo>());
			
			indexInfoList.add(currentIndexInfo);
		}
		 
		ObjectColumnInfo column = new ObjectColumnInfo();
		
		column.setName(rowData.getString(MetaColumnConstants.COLUMN_NAME));
		column.setNo(rowData.getInt(MetaColumnConstants.ORDINAL_POSITION));
		column.setAscOrdesc(rowData.getString(MetaColumnConstants.ASC_OR_DESC));
		
		currentIndexInfo.addColInfo(column);
		
		beforeTableName = indexName;
		
	}

}
