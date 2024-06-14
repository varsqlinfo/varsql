package com.varsql.core.db.datatype;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.varsql.core.common.util.TimeZoneUtils;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.db.datatype.handler.MetaDataHandler;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.datatype.handler.StatementHandler;

public enum DefaultDataType implements DataType {
	
	BIT(Types.BIT, DBColumnMetaInfo.BIT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.BIT, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getObject(columnIndex);
			}
		}).build()
	),
	TINYINT(Types.TINYINT, DBColumnMetaInfo.TINYINT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.TINYINT, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getByte(columnIndex);
			}
		}).build()
	),
	SMALLINT(Types.SMALLINT, DBColumnMetaInfo.SMALLINT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.SMALLINT, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getShort(columnIndex);
			}
		}).build()
	),
	INTEGER(Types.INTEGER, DBColumnMetaInfo.INTEGER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.INTEGER, value)) return ;
				
				if(value instanceof String) {
					pstmt.setInt(parameterIndex, Integer.parseInt(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
				
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getInt(columnIndex);
			}
		}).build()
	),
	// custom default type 추가. int
	INT(Types.INTEGER, DBColumnMetaInfo.INT, INTEGER.getDataTypeHandler()),
	BIGINT(Types.BIGINT, DBColumnMetaInfo.BIGINT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.BIGINT, value)) return ;
				
				if(value instanceof String) {
					pstmt.setLong(parameterIndex, Long.parseLong(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getLong(columnIndex);
			}
		}).build()
	),
	FLOAT(Types.FLOAT, DBColumnMetaInfo.FLOAT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.FLOAT, value)) return ;
				
				if(value instanceof String) {
					pstmt.setFloat(parameterIndex, Float.parseFloat(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getFloat(columnIndex);
			}
		}).build()
	),
	REAL(Types.REAL, DBColumnMetaInfo.FLOAT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.REAL, value)) return ;
				
				if(value instanceof String) {
					pstmt.setFloat(parameterIndex, Float.parseFloat(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getFloat(columnIndex);
			}
		}).build()
	),
	DOUBLE(Types.DOUBLE, DBColumnMetaInfo.DOUBLE, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.DOUBLE, value)) return ;
				
				if(value instanceof String) {
					pstmt.setDouble(parameterIndex, Double.parseDouble(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getDouble(columnIndex);
			}
		}).build()
	),
	NUMERIC(Types.NUMERIC, DBColumnMetaInfo.NUMERIC, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.NUMERIC, value)) return ;
				
				if(value instanceof String) {
					pstmt.setBigDecimal(parameterIndex, new BigDecimal(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Object reval = rs.getObject(columnIndex);
				
				if(reval ==null) {
					return null; 
				}
				
				if(reval instanceof Long) {
					return reval;
				}
				
				BigDecimal bd = (BigDecimal)reval; 
				
				if( bd == BigDecimal.ZERO) {
					return 0; 
				}
				
				if(bd.signum() == 0 || bd.scale() <=0 || bd.stripTrailingZeros().scale() <= 0) {
					try {
						return bd.toBigInteger();
					}catch(Exception e) {
						e.printStackTrace();
						return bd;
					}
				}
				
				return bd;
			}
		}).build()
	),
	DECIMAL(Types.DECIMAL, DBColumnMetaInfo.DECIMAL, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.DECIMAL, value)) return ;
				
				if(value instanceof String) {
					pstmt.setBigDecimal(parameterIndex, new BigDecimal(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				BigDecimal bd = rs.getBigDecimal(columnIndex); 
				
				if(bd == null || bd == BigDecimal.ZERO) {
					return 0; 
				}
				
				if(bd.signum() == 0 || bd.scale() <=0 || bd.stripTrailingZeros().scale() <= 0) {
					try {
						return bd.toBigInteger();
					}catch(Exception e) {
						e.printStackTrace();
						return bd;
					}
				}
				
				return bd;
			}
		}).build()
	),
	CHAR(Types.CHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.CHAR, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getString(columnIndex);
			}
		}).build()
	),
	VARCHAR(Types.VARCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.VARCHAR, value)) return ;
			
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getString(columnIndex);
			}
		}).build()
	, false, VARCHAR_DEFAULT_SIZE),
	LONGVARCHAR(Types.LONGVARCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.LONGVARCHAR, value)) return ;
			
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getString(columnIndex);
			}
		}).build()
	),
	DATE(Types.DATE, DBColumnMetaInfo.DATE, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.DATE, value)) return ;
				
	            if(value instanceof Long) {
            	  pstmt.setObject(parameterIndex, new Date(Long.parseLong(value.toString())));
            	}else if(value instanceof String) {
					pstmt.setDate(parameterIndex, new Date(VarsqlDateUtils.stringToDate(value.toString()).getTime()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Date val = rs.getDate(columnIndex);
				
				if(isNull(val)) return null;
				
				if(!isFormatValue) {
					return val.getTime();
				}
				
				return VarsqlDateUtils.dateFormat(val);
			}
		}).build()
	),
	TIME(Types.TIME, DBColumnMetaInfo.TIME, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.TIME, value)) return ;
				
	            if(value instanceof Long) {
					pstmt.setObject(parameterIndex, new Time(Long.parseLong(value.toString())));
				}else if(value instanceof Integer) {
					pstmt.setObject(parameterIndex, new Time(Integer.parseInt(value.toString())));
				}else if(value instanceof String) {
	            	Time time = null;
					try {
						time = Time.valueOf(value.toString());
					}catch(IllegalArgumentException e) {
						try {
							time = VarsqlDateUtils.stringToTime(value.toString());
						}catch(Exception e1) {
							System.out.println("time set parameter error : "+e1.getMessage());
						}
					}
					pstmt.setTime(parameterIndex, time);
				}else {
					pstmt.setObject(parameterIndex, value); 
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Time val = rs.getTime(columnIndex);
				
				if(isNull(val)) return null;
				
				if(!isFormatValue) {
					return val.getTime();
				}
				
				return VarsqlDateUtils.timeFormat(val);
			}
		}).build()
	),
	TIMESTAMP(Types.TIMESTAMP, DBColumnMetaInfo.TIMESTAMP, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.TIMESTAMP, value)) return ;
				
	            if(value instanceof Long) {
					pstmt.setObject(parameterIndex, new Timestamp(Long.parseLong(value.toString())));
				}else if(value instanceof String) {
					Timestamp timestamp = null;
					try {
						timestamp = Timestamp.valueOf(value.toString());
					}catch(IllegalArgumentException e) {
						try {
							timestamp = VarsqlDateUtils.millisecondStringToTimestamp(value.toString());
						}catch(Exception e1) {
							timestamp = VarsqlDateUtils.stringToTimestamp(value.toString());
						}
					}
					pstmt.setTimestamp(parameterIndex, timestamp);
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Timestamp val = rs.getTimestamp(columnIndex);
				
				if(isNull(val)) return null;
				
				if(!isFormatValue) {
					return val.getTime();
				}
				
				return VarsqlDateUtils.timestampMilliFormat(val);
			}
		}).build()
	),
	// custom default type 추가. datatype
	DATETIME(Types.TIMESTAMP, DBColumnMetaInfo.TIMESTAMP, TIMESTAMP.getDataTypeHandler()),
	BINARY(Types.BINARY, DBColumnMetaInfo.BINARY, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.BINARY, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				if(isNull(rs.getBinaryStream(columnIndex))) return null;
				
				return dataType.getTypeName() + " BINARY";
			}
		}).build()
	),
	VARBINARY(Types.VARBINARY, DBColumnMetaInfo.BINARY, BINARY.getDataTypeHandler()),
	LONGVARBINARY(Types.LONGVARBINARY, DBColumnMetaInfo.BINARY, BINARY.getDataTypeHandler()),
	
	JAVA_OBJECT(Types.JAVA_OBJECT, DBColumnMetaInfo.OTHER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.JAVA_OBJECT, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getObject(columnIndex);
			}
		}).build()
	),
	DISTINCT(Types.DISTINCT, DBColumnMetaInfo.OTHER),
	STRUCT(Types.STRUCT, DBColumnMetaInfo.OTHER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.STRUCT, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getObject(columnIndex);
			}
		}).build()
	),
	ARRAY(Types.ARRAY, DBColumnMetaInfo.ARRAY, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.ARRAY, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Array reval = rs.getArray(columnIndex);
				return reval==null ? null : reval.getArray();
			}
		}).build()
	),
	BLOB(Types.BLOB, DBColumnMetaInfo.BLOB, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.BLOB, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				if(isNull(rs.getBlob(columnIndex))) return null;
				
				return dataType.getTypeName();
			}
		}).build()
	),
	CLOB(Types.CLOB, DBColumnMetaInfo.CLOB, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.CLOB, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			
			@Override
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				try(Reader val = rs.getCharacterStream(columnIndex);){
					
					if(isNull(val)) return null;
					
					char[] buffer  = null;
					int byteRead=-1;
					StringBuffer output = new StringBuffer();
					
					buffer = new char[1024];
					while((byteRead=val.read(buffer,0,1024))!=-1){
						output.append(buffer,0,byteRead);
					}
					val.close();
					return output.toString();
				}catch(Exception e){
					if(dert.equals(DataExceptionReturnType.NULL)) {
						return null;
					}else if(dert.equals(DataExceptionReturnType.EMTPY_STRING)) {
						return "";
					}else if(dert.equals(DataExceptionReturnType.TYPE_NAME)) {
						return "Clob";
					}else {
						return "Clob" +e.getMessage();
					}
				}
			}
		}).build()
	),
	REF(Types.REF, DBColumnMetaInfo.OTHER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.REF, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getRef(columnIndex);
			}
		}).build()
	),
	DATALINK(Types.DATALINK, DBColumnMetaInfo.OTHER),
	BOOLEAN(Types.BOOLEAN, DBColumnMetaInfo.BIT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.BOOLEAN, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getBoolean(columnIndex);
			}
		}).build()
	),
	ROWID(Types.ROWID, DBColumnMetaInfo.OTHER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.ROWID, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getRowId(columnIndex);
			}
		}).build()
	),
	NCHAR(Types.NCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.NCHAR, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getNString(columnIndex);
			}
		}).build()
	),
	NVARCHAR(Types.NVARCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.NVARCHAR, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getNString(columnIndex);
			}
		}).build()
	),
	LONGNVARCHAR(Types.LONGNVARCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.LONGNVARCHAR, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getNString(columnIndex);
			}
		}).build()
	),
	NCLOB(Types.NCLOB, DBColumnMetaInfo.CLOB, CLOB.getDataTypeHandler()),
	SQLXML(Types.SQLXML, DBColumnMetaInfo.SQLXML, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.SQLXML, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				SQLXML val  = rs.getSQLXML(columnIndex);
				
				if(isNull(val)) return null;
				
				return val.getString();
			}
		}).build()
	),
	REF_CURSOR(Types.REF_CURSOR, DBColumnMetaInfo.OTHER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.REF_CURSOR, value)) return ;
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				return rs.getObject(columnIndex);
			}
		}).build()
	),
	TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE, DBColumnMetaInfo.TIME, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.TIME_WITH_TIMEZONE, value)) return ;
				
	            if(value instanceof Long) {
					pstmt.setObject(parameterIndex, new Time(Long.parseLong(value.toString())));
				}else if(value instanceof Integer) {
					pstmt.setObject(parameterIndex, new Time(Integer.parseInt(value.toString())));
				}else if(value instanceof String) {
					pstmt.setObject(parameterIndex, VarsqlDateUtils.stringToTime(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Time val = rs.getTime(columnIndex);
				
				if(isNull(val)) return null;
				
				if(!isFormatValue) {
					return val.getTime();
				}
				
				return val.toLocalTime().toString();
			}
		}).build()
	),
	TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE, DBColumnMetaInfo.TIMESTAMP, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.TIMESTAMP_WITH_TIMEZONE, value)) return ;
				
	            if(value instanceof Long) {
	            	  pstmt.setObject(parameterIndex, new Timestamp(Long.parseLong(value.toString())));
	            }else if(value instanceof String) {
					pstmt.setObject(parameterIndex, VarsqlDateUtils.stringToTimestamp(value.toString()));
				}else {
					pstmt.setObject(parameterIndex, value);
				}
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert, boolean isFormatValue) throws SQLException {
				Timestamp val = rs.getTimestamp(columnIndex);
				
				if(isNull(val)) return null;
				
				if(!isFormatValue) {
					return val.getTime();
				}
				
				return val.toInstant().atZone(TimeZoneUtils.getZoneId(null)).toString();
			}
		}).build()
	),
	OTHER(Types.OTHER, DBColumnMetaInfo.OTHER);
	
	private int typeCode; // sql type code
	private int defaultSize; // char default size
	private DBColumnMetaInfo jdbcDataTypeMetaInfo; 
	private DataTypeHandler dataTypeHandler;	// data type handler
	private boolean excludeImportColumn;	// file import exclude flag

	private final static Map<Integer, DefaultDataType> allTypeInfo = new HashMap<Integer, DefaultDataType>();

	static {
		for (DefaultDataType type : DefaultDataType.values()) {
			allTypeInfo.put(type.typeCode, type);
		}
	}
	
	private DefaultDataType(int code, DBColumnMetaInfo jdbcDataTypeMetaInfo) {
		this(code, jdbcDataTypeMetaInfo, null);
	}
	
	private DefaultDataType(int code, DBColumnMetaInfo jdbcDataTypeMetaInfo, DataTypeHandler dataTypeHandler) {
		this(code, jdbcDataTypeMetaInfo, dataTypeHandler, false);
	}
	private DefaultDataType(int code, DBColumnMetaInfo jdbcDataTypeMetaInfo, DataTypeHandler dataTypeHandler, boolean excludeImportColumn) {
		this(code, jdbcDataTypeMetaInfo, dataTypeHandler, false, -1);
	}
	private DefaultDataType(int code, DBColumnMetaInfo jdbcDataTypeMetaInfo, DataTypeHandler dataTypeHandler, boolean excludeImportColumn, int defaultSize) {
		this.typeCode = code;
		this.jdbcDataTypeMetaInfo = jdbcDataTypeMetaInfo;
		this.dataTypeHandler = dataTypeHandler != null ? dataTypeHandler : DataTypeHandler.builder().build();
		this.excludeImportColumn = excludeImportColumn;
		this.defaultSize = defaultSize;
	}

	@Override
	public int getTypeCode() {
		return this.typeCode;
	}
	
	@Override
	public int getDefaultSize() {
		return this.defaultSize;
	}

	@Override
	public String getTypeName() {
		return this.name();
	}

	@Override
	public DBColumnMetaInfo getJDBCDataTypeMetaInfo() {
		return this.jdbcDataTypeMetaInfo;
	}

	@Override
	public StatementHandler getStatementHandler() {
		return this.dataTypeHandler.getStatementHandler();
	}

	@Override
	public ResultSetHandler getResultSetHandler() {
		return this.dataTypeHandler.getResultSetHandler();
	}
	
	@Override
	public MetaDataHandler getMetaDataHandler() {
		return this.dataTypeHandler.getMetaDataHandler();
	}
	
	public DataTypeHandler getDataTypeHandler() {
		return this.dataTypeHandler;
	}
	
	public static DataType getDataType(String typeName) {
		for (DefaultDataType datatype : DefaultDataType.values()) {
			if(datatype.name().equalsIgnoreCase(typeName)) {
				return datatype;
			}
		}
		return DefaultDataType.OTHER;
	}
	
	public static DataType getDataType(int typeCode) {
		for (DefaultDataType datatype : DefaultDataType.values()) {
			if(datatype.getTypeCode() == typeCode) {
				return datatype;
			}
		}
		
		return DefaultDataType.OTHER;
	}
	
	public static DefaultDataType getDefaultDataType(String typeName) {
		for (DefaultDataType datatype : DefaultDataType.values()) {
			if(datatype.name().equalsIgnoreCase(typeName)) {
				return datatype;
			}
		}
		return DefaultDataType.OTHER;
	}
	
	public static DefaultDataType getDefaultDataType(int typeCode) {
		for (DefaultDataType datatype : DefaultDataType.values()) {
			if(datatype.getTypeCode() == typeCode) {
				return datatype;
			}
		}
		return DefaultDataType.OTHER;
	}
	
	private static boolean isNull(Object value) throws SQLException {
		return value ==null ? true:false;
	}
	private static boolean setNullValue(PreparedStatement pstmt, int parameterIndex, int sqlType, Object value) throws SQLException {
		if(value ==null){
			pstmt.setNull(parameterIndex, sqlType);
			return true; 
		}
		
		return false;
	}

	@Override
	public boolean isExcludeImportColumn() {
		return excludeImportColumn;
	}
}