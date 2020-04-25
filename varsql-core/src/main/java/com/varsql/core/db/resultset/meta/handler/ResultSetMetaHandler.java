package com.varsql.core.db.resultset.meta.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.valueobject.ColumnInfo;

public interface ResultSetMetaHandler{
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl, Set keyColumn) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl, Set keyColumn, Map colComment) throws SQLException;
}