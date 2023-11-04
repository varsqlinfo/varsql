package com.varsql.core.db.datatype.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.varsql.core.db.datatype.DataExceptionReturnType;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DefaultDataType;

public interface ResultSetHandler {
	default Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException{
		return getValue(dataType, rs, columnIndex, dert, false);
	}
	
	default Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean exportFlag) throws SQLException{
		Object obj = rs.getObject(columnIndex);
		
		if(obj == null) return null; 
		
		Class<?> superClass = obj.getClass().getSuperclass(); 
		
		if(superClass== java.io.Reader.class) {
			return DefaultDataType.CLOB.getResultSetHandler().getValue(dataType, rs, columnIndex, dert, exportFlag);
		}
		
		if(superClass == java.io.InputStream.class) {
			return dataType.getTypeName() + " BINARY";
		}
		
		return obj.toString();
	}

	default Object getValue(DataType dataType, ResultSet rs, String columnLabel, DataExceptionReturnType dert) throws SQLException {
		return getValue(dataType, rs, columnLabel, dert, false);
	}

	default Object getValue(DataType dataType, ResultSet rs, String columnLabel, DataExceptionReturnType dert, boolean exportFlag) throws SQLException{
		Object obj = rs.getObject(columnLabel);
		
		if(obj == null) return null; 
		
		Class<?> superClass = obj.getClass().getSuperclass(); 
		
		if(superClass == java.io.Reader.class) {
			return DefaultDataType.CLOB.getResultSetHandler().getValue(dataType, rs, columnLabel, dert, exportFlag);
		}
		
		if(obj.getClass().getSuperclass() == java.io.InputStream.class) {
			return dataType.getTypeName() + " BINARY";
		}
		
		return obj.toString();
	}
}

