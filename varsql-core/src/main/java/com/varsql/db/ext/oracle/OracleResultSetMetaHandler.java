package com.varsql.db.ext.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.common.util.StringUtil;
import com.varsql.core.db.beans.ColumnInfo;
import com.varsql.core.db.beans.DataTypeInfo;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.resultset.meta.handler.ResultSetMetaHandlerImpl;
import com.varsql.core.db.util.DbMetaUtils;

public class OracleResultSetMetaHandler extends ResultSetMetaHandlerImpl{
	@Override
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl,Set keyColumn , Map colComment) throws SQLException{
		ColumnInfo column = new ColumnInfo();
		
		String cName=  rs.getString(MetaColumnConstants.COLUMN_NAME);
		String degitsLen= StringUtil.nullToString(rs.getString(MetaColumnConstants.DECIMAL_DIGITS));
		String dataType = rs.getString(MetaColumnConstants.DATA_TYPE); 
		DataTypeInfo dataTypeInfo = dataTypeImpl.getDataType(dataType);
		
		column.setName(cName);
		column.setDataType(dataType);
		column.setLength(rs.getInt(MetaColumnConstants.COLUMN_SIZE));
		column.setDefaultVal(StringUtil.nullToString(rs.getString(MetaColumnConstants.COLUMN_DEF)));
		column.setNullable(StringUtil.nullToString(rs.getString(MetaColumnConstants.IS_NULLABLE)));
		column.setAutoincrement("");
		column.setTypeName(dataTypeInfo.getDataTypeName());
		column.setTypeAndLength(DbMetaUtils.getTypeName(dataTypeInfo ,column ,dataTypeInfo.getDataTypeName(), degitsLen));
		
		if(colComment != null){
			column.setComment(StringUtil.nullToString(colComment.get(cName)!=null? (String)colComment.get(cName) : rs.getString(MetaColumnConstants.REMARKS)));
		}
		
		column.setConstraints(keyColumn.contains(cName)?"PK":"");
		
		return column; 
	
	}
}