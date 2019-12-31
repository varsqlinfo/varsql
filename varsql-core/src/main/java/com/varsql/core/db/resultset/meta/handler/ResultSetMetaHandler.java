package com.varsql.core.db.resultset.meta.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.db.beans.ColumnInfo;
import com.varsql.core.db.meta.datatype.DataTypeImpl;

public interface ResultSetMetaHandler{
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl, Set keyColumn) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl, Set keyColumn, Map colComment) throws SQLException;
}