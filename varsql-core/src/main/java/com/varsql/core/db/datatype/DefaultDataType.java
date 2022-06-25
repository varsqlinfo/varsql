package com.varsql.core.db.datatype;

import java.io.Reader;
import java.math.BigDecimal;
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getObject(columnIndex);
			}
		}).build()
	),
	TINYINT(Types.TINYINT, DBColumnMetaInfo.TINYINT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.TINYINT, value)) return ;
				
				pstmt.setByte(parameterIndex, (Byte)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getByte(columnIndex);
			}
		}).build()
	),
	SMALLINT(Types.SMALLINT, DBColumnMetaInfo.SMALLINT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.SMALLINT, value)) return ;
				
				pstmt.setShort(parameterIndex, (short)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getShort(columnIndex);
			}
		}).build()
	),
	INTEGER(Types.INTEGER, DBColumnMetaInfo.INTEGER, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.INTEGER, value)) return ;
				
				pstmt.setInt(parameterIndex, (int)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getInt(columnIndex);
			}
		}).build()
	),
	// custom default type 추가. int
	INT(Types.INTEGER, DBColumnMetaInfo.INT, INTEGER.getDataTypeHandler()),
	BIGINT(Types.BIGINT, DBColumnMetaInfo.BIGINT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.BIGINT, value)) return ;
				
				pstmt.setLong(parameterIndex, (long)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getLong(columnIndex);
			}
		}).build()
	),
	FLOAT(Types.FLOAT, DBColumnMetaInfo.FLOAT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.FLOAT, value)) return ;
				
				pstmt.setFloat(parameterIndex, (float)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getFloat(columnIndex);
			}
		}).build()
	),
	REAL(Types.REAL, DBColumnMetaInfo.FLOAT, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.REAL, value)) return ;
				
				pstmt.setFloat(parameterIndex, (float)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getFloat(columnIndex);
			}
		}).build()
	),
	DOUBLE(Types.DOUBLE, DBColumnMetaInfo.DOUBLE, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.DOUBLE, value)) return ;
				
				pstmt.setDouble(parameterIndex, (double)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getDouble(columnIndex);
			}
		}).build()
	),
	NUMERIC(Types.NUMERIC, DBColumnMetaInfo.NUMERIC, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.NUMERIC, value)) return ;
				
				pstmt.setBigDecimal(parameterIndex, (BigDecimal)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getBigDecimal(columnIndex);
			}
		}).build()
	),
	DECIMAL(Types.DECIMAL, DBColumnMetaInfo.DECIMAL, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.DECIMAL, value)) return ;
				
				pstmt.setBigDecimal(parameterIndex, (BigDecimal)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getBigDecimal(columnIndex);
			}
		}).build()
	),
	CHAR(Types.CHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.CHAR, value)) return ;
				
				pstmt.setString(parameterIndex, (String)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getString(columnIndex);
			}
		}).build()
	),
	VARCHAR(Types.VARCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.VARCHAR, value)) return ;
			
				pstmt.setString(parameterIndex, (String)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getString(columnIndex);
			}
		}).build()
	),
	LONGVARCHAR(Types.LONGVARCHAR, DBColumnMetaInfo.STRING, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				if(setNullValue(pstmt, parameterIndex, Types.LONGVARCHAR, value)) return ;
			
				pstmt.setString(parameterIndex, (String)value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getString(columnIndex);
			}
		}).build()
	),
	DATE(Types.DATE, DBColumnMetaInfo.DATE, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.DATE, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				Date val = rs.getDate(columnIndex);
				
				if(isNull(val)) return null;
				
				return VarsqlDateUtils.dateFormat(val);
			}
		}).build()
	),
	TIME(Types.TIME, DBColumnMetaInfo.TIME, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.TIME, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				Time val = rs.getTime(columnIndex);
				
				if(isNull(val)) return null;
				
				return VarsqlDateUtils.timeFormat(val);
			}
		}).build()
	),
	TIMESTAMP(Types.TIMESTAMP, DBColumnMetaInfo.TIMESTAMP, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.TIMESTAMP, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				Timestamp val = rs.getTimestamp(columnIndex);
				
				if(isNull(val)) return null;
				
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getArray(columnIndex);
			}
		}).build()
	),
	BLOB(Types.BLOB, DBColumnMetaInfo.BLOB, DataTypeHandler.builder().statementHandler(new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
	            if(setNullValue(pstmt, parameterIndex, Types.BLOB, value)) return ;
				
				pstmt.setObject(parameterIndex, value);
			}
		}).resultSetHandler(new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
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
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getObject(columnIndex);
			}
		}).build()
	),
	TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE, DBColumnMetaInfo.OTHER),
	TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE, DBColumnMetaInfo.OTHER),
	OTHER(Types.OTHER, DBColumnMetaInfo.OTHER);
	
	private int typeCode;
	private DBColumnMetaInfo jdbcDataTypeMetaInfo;
	private DataTypeHandler dataTypeHandler;

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
		this.typeCode = code;
		this.jdbcDataTypeMetaInfo = jdbcDataTypeMetaInfo;
		this.dataTypeHandler = dataTypeHandler != null ? dataTypeHandler : DataTypeHandler.builder().build();
	}

	@Override
	public int getTypeCode() {
		return this.typeCode;
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
	
	public static DefaultDataType getDefaultDataType(String typeName) {
		for (DefaultDataType datatype : DefaultDataType.values()) {
			if(datatype.name().equalsIgnoreCase(typeName)) {
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
}