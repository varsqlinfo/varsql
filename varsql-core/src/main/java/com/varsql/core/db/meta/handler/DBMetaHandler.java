package com.varsql.core.db.meta.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.valueobject.ColumnInfo;

public interface DBMetaHandler{
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory, Set keyColumn) throws SQLException;
	
	public ColumnInfo getColumnInfo(ResultSet rs, DataTypeFactory dataTypeFactory, Set keyColumn, Map colComment) throws SQLException;
}