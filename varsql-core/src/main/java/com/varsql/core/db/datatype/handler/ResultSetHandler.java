package com.varsql.core.db.datatype.handler;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varsql.core.db.datatype.DataExceptionReturnType;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DefaultDataType;

public interface ResultSetHandler {
	default Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException{
		return getValue(dataType, rs, columnIndex, dert, true);
	}
	
	default Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException{
		Object obj = rs.getObject(columnIndex);
		
		if(obj == null) return null; 
		
		Class<?> superClass = obj.getClass().getSuperclass(); 
		
		if(superClass== java.io.Reader.class || obj instanceof Clob) {
			return DefaultDataType.CLOB.getResultSetHandler().getValue(dataType, rs, columnIndex, dert, isFormatValue);
		}
		
		if(superClass == java.io.InputStream.class || obj instanceof Blob) {
			return dataType.getTypeName() + " BINARY";
		}
		
		try {
			return obj.toString();
		}catch(Exception e) {
			return "varsql value error : "+ obj.getClass();
		}
	}

	default Object getValue(DataType dataType, ResultSet rs, String columnLabel, DataExceptionReturnType dert) throws SQLException {
		return getValue(dataType, rs, columnLabel, dert, true);
	}

	default Object getValue(DataType dataType, ResultSet rs, String columnLabel, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException{
		Object obj = rs.getObject(columnLabel);
		
		if(obj == null) return null; 
		
		Class<?> superClass = obj.getClass().getSuperclass(); 
		
		if(superClass== java.io.Reader.class || obj instanceof Clob) {
			return DefaultDataType.CLOB.getResultSetHandler().getValue(dataType, rs, columnLabel, dert, isFormatValue);
		}
		
		if(superClass == java.io.InputStream.class || obj instanceof Blob) {
			return dataType.getTypeName() + " BINARY";
		}
		
		try {
			return obj.toString();
		}catch(Exception e) {
			return "varsql value error : "+ obj.getClass();
		}
	}
}

