package com.varsql.core.sql.type;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public enum JdbcType {
	ARRAY(Types.ARRAY, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setArray(parameterIndex, (java.sql.Array)obj);
		}
	}),
	BIT(Types.BIT, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setByte(parameterIndex, (byte) obj);
		}
	}),
	TINYINT(Types.TINYINT, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setInt(parameterIndex, (int) obj);
		}
	}),
	SMALLINT(Types.SMALLINT, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setInt(parameterIndex, (int) obj);
		}
	}),
	INTEGER(Types.INTEGER, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setInt(parameterIndex, (int) obj);
		}
	}),
	BIGINT(Types.BIGINT, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setInt(parameterIndex, (int) obj);
		}
	}),
	FLOAT(Types.FLOAT, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setFloat(parameterIndex, (float) obj);
		}
	}),
	REAL(Types.REAL),
	DOUBLE(Types.DOUBLE, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setDouble(parameterIndex, (double) obj);
		}
	}),
	NUMERIC(Types.NUMERIC, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setInt(parameterIndex, (int) obj);
		}
	}),
	DECIMAL(Types.DECIMAL, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setBigDecimal(parameterIndex, (BigDecimal) obj);
		}
	}),
	CHAR(Types.CHAR, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setString(parameterIndex, (String) obj);
		}
	}),
	VARCHAR(Types.VARCHAR, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setString(parameterIndex, (String) obj);
		}
	}),
	LONGVARCHAR(Types.LONGVARCHAR, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setString(parameterIndex, (String) obj);
		}
	}),
	DATE(Types.DATE, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setDate(parameterIndex, (Date) obj);
		}
	}),
	TIME(Types.TIME, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setTime(parameterIndex, (Time) obj);
		}
	}),
	TIMESTAMP(Types.TIMESTAMP, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setTimestamp(parameterIndex, (Timestamp) obj);
		}
	}),
	BINARY(Types.BINARY, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setBinaryStream(parameterIndex, (InputStream) obj);
		}
	}),
	VARBINARY(Types.VARBINARY, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setBinaryStream(parameterIndex, (InputStream) obj);
		}
	}),
	LONGVARBINARY(Types.LONGVARBINARY, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setBinaryStream(parameterIndex, (InputStream) obj);
		}
	}),
	NULL(Types.NULL, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setNull(parameterIndex, Types.NULL);
		}
	}),
	BLOB(Types.BLOB, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setBlob(parameterIndex, (Blob)obj);
		}
	}),
	CLOB(Types.CLOB, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setClob(parameterIndex, (Clob)obj);
		}
	}),
	BOOLEAN(Types.BOOLEAN, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setBoolean(parameterIndex, (Boolean)obj);
		}
	}),
	NVARCHAR(Types.NVARCHAR, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setNString(parameterIndex, (String)obj);
		}
	}), 
	NCHAR(Types.NCHAR, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setNString(parameterIndex, (String)obj);
		}
	}), 
	NCLOB(Types.NCLOB, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setNClob(parameterIndex, (NClob)obj);
		}
	}), 
	JAVA_OBJECT(Types.JAVA_OBJECT),
	LONGNVARCHAR(Types.LONGNVARCHAR), 
	SQLXML(Types.SQLXML, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setSQLXML(parameterIndex, (java.sql.SQLXML)obj);
		}
	}),
	OTHER(Types.OTHER, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setObject(parameterIndex, obj);
		}
	}),
	OBJECT(99999, new StatementHandler() {
		public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
			pstmt.setObject(parameterIndex, obj);
		}
	});
	
	public final int TYPE_CODE;
	public final StatementHandler statementHandler;
	
	private final static Map<String, JdbcType> allTypeInfo = new HashMap<String, JdbcType>();

	static {
		for (JdbcType type : JdbcType.values()) {
			allTypeInfo.put(type.name(), type);
		}
	}

	private JdbcType(int code) {
		this.TYPE_CODE = code;
		this.statementHandler  =  new StatementHandler(){
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object obj) throws SQLException {
				pstmt.setObject(parameterIndex, obj);
			}
		};
	}
	private JdbcType(int code, StatementHandler statementHandler) {
		this.TYPE_CODE = code;
		this.statementHandler = statementHandler; 
	}

	public static JdbcType getCode(String jdbcCode) {
		if(allTypeInfo.containsKey(jdbcCode)) {
			return allTypeInfo.get(jdbcCode);
		}else {
			return JdbcType.OBJECT;
		}
	}
	
	public void setParameter(PreparedStatement pstmt,int parameterIndex, Object obj) throws SQLException {
		this.statementHandler.setParameter(pstmt, parameterIndex, obj);
	}
}

interface StatementHandler{
	void setParameter(PreparedStatement pstmt,int parameterIndex, Object obj) throws SQLException;
}