package com.varsql.core.db.resultset.meta.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.common.util.StringUtil;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DataTypeInfo;

/**
 * 
 * @FileName  : ResultSetMetaHandlerImpl.java
 * @프로그램 설명 :  result meta 얻기.
 * @Date      : 2016. 11. 20. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class ResultSetMetaHandlerImpl implements ResultSetMetaHandler{
	/**
	 * 
	 * @Method Name  : getColumnInfo
	 * @Method 설명 : 컬럼 정보 얻기. 
	 * @Method override : @see com.varsql.db.resultset.meta.handler.ResultSetMetaHandler#getColumnInfo(java.sql.ResultSet, com.varsql.db.meta.datatype.DataTypeImpl, java.util.Set)
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 20. 
	 * @변경이력  :
	 * @param rs
	 * @param dataTypeImpl
	 * @param keyColumn
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl) throws SQLException{
		return getColumnInfo(rs, dataTypeImpl, null);
	}
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl,Set keyColumn) throws SQLException{
		return getColumnInfo(rs, dataTypeImpl,keyColumn , null); 
	}
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl,Set keyColumn , Map colComment) throws SQLException{
		ColumnInfo column = new ColumnInfo();
		
		String cName=  rs.getString(MetaColumnConstants.COLUMN_NAME);
		String dataType = rs.getString(MetaColumnConstants.DATA_TYPE); 
		
		String degitsLen= StringUtil.nullToString(rs.getString(MetaColumnConstants.DECIMAL_DIGITS));
		String columnSize= rs.getString(MetaColumnConstants.COLUMN_SIZE);
		DataTypeInfo dataTypeInfo = dataTypeImpl.getDataType(dataType);
		
		column.setName(cName);
		column.setDataType(dataType);
		column.setLength(rs.getInt(MetaColumnConstants.COLUMN_SIZE));
		column.setDefaultVal(StringUtil.nullToString(rs.getString(MetaColumnConstants.COLUMN_DEF)));
		column.setNullable(StringUtil.nullToString(rs.getString(MetaColumnConstants.IS_NULLABLE)));
		column.setAutoincrement("");
		column.setTypeName(dataTypeInfo.getDataTypeName());
		column.setTypeAndLength(DbMetaUtils.getTypeName(dataTypeInfo ,column ,dataTypeInfo.getDataTypeName(), columnSize ,degitsLen));
		
		if(colComment != null){
			column.setComment(StringUtil.nullToString(colComment.get(cName)!=null? (String)colComment.get(cName) : rs.getString(MetaColumnConstants.REMARKS)));
		}
		
		if(keyColumn !=null){
			column.setConstraints(keyColumn.contains(cName)?"PK":"");
		}
		return column; 
	}
}