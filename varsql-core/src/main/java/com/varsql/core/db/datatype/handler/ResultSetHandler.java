package com.varsql.core.db.datatype.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.varsql.core.db.datatype.DataExceptionReturnType;
import com.varsql.core.db.datatype.DataType;

public interface ResultSetHandler {
	Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException;
	
	default Object getValue(DataType dataType, ResultSet rs, String columnLabel, DataExceptionReturnType dert) throws SQLException {
		return rs.getObject(columnLabel);
	}
}

