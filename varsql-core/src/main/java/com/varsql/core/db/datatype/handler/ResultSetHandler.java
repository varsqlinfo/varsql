package com.varsql.core.db.datatype.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.varsql.core.db.datatype.DataExceptionReturnType;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DefaultDataType;

public interface ResultSetHandler {
	default Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException{

		Object obj = rs.getObject(columnIndex);
		
		if(obj == null) return null; 
		
		if(obj.getClass().getSuperclass() == java.io.Reader.class) {
			return DefaultDataType.CLOB.getResultSetHandler().getValue(dataType, rs, columnIndex, dert);
		}
		
		if(obj.getClass().getSuperclass() == java.io.InputStream.class) {
			return dataType.getTypeName() + " BINARY";
		}
		
		return obj;
	}
	
	default Object getValue(DataType dataType, ResultSet rs, String columnLabel, DataExceptionReturnType dert) throws SQLException {
		
		Object obj = rs.getObject(columnLabel);
		
		if(obj == null) return null; 
		
		if(obj.getClass().getSuperclass() == java.io.Reader.class) {
			return DefaultDataType.CLOB.getResultSetHandler().getValue(dataType, rs, columnLabel, dert);
		}
		
		if(obj.getClass().getSuperclass() == java.io.InputStream.class) {
			return dataType.getTypeName() + " BINARY";
		}
		
		return obj;
	}
}

