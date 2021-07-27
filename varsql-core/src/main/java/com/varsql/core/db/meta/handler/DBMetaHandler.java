package com.varsql.core.db.meta.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.valueobject.ColumnInfo;

public interface DBMetaHandler{
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl, Set keyColumn) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeImpl dataTypeImpl, Set keyColumn, Map colComment) throws SQLException;
}