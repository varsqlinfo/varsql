package com.varsql.core.db.datatype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varsql.core.db.datatype.handler.MetaDataHandler;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.datatype.handler.StatementHandler;

public class DataTypeHandler {
	
	private StatementHandler statementHandler;
	private ResultSetHandler resultSetHandler;
	private MetaDataHandler metaDataHandler;
	
	public DataTypeHandler(StatementHandler statementHandler, ResultSetHandler resultSetHandler,
			MetaDataHandler metaDataHandler) {
		
		this.statementHandler = statementHandler != null ? statementHandler : getDefaultStatementHandler();
		this.resultSetHandler = resultSetHandler != null? resultSetHandler : getDefaultResultSetHandler();
		this.metaDataHandler = metaDataHandler != null? metaDataHandler : getDefaultMetaDataHandler();
	}
	
	public StatementHandler getStatementHandler() {
		return statementHandler;
	}

	public ResultSetHandler getResultSetHandler() {
		return resultSetHandler;
	}

	public MetaDataHandler getMetaDataHandler() {
		return metaDataHandler;
	}

	public static DataTypeHandlerBuilder builder() {
		return new DataTypeHandlerBuilder();
	}
	
	private static StatementHandler getDefaultStatementHandler() {
		return new StatementHandler() {
			public void setParameter(PreparedStatement pstmt, int parameterIndex, Object value) throws SQLException {
				pstmt.setObject(parameterIndex, value);
			}
		};
	}
	
	private static ResultSetHandler getDefaultResultSetHandler() {
		return new ResultSetHandler() {
			public Object getValue(DataType dataType, ResultSet rs, int columnIndex, DataExceptionReturnType dert) throws SQLException {
				return rs.getObject(columnIndex);
			}
		};
	}
	
	private static MetaDataHandler getDefaultMetaDataHandler() {
		return new MetaDataHandler() {
			public int getColumnLength(int length){
				return length;
			}
		};
	}
	
	public static class DataTypeHandlerBuilder{
		private StatementHandler statementHandler;
		private ResultSetHandler resultSetHandler;
		private MetaDataHandler metaDataHandler;
		
		public DataTypeHandlerBuilder statementHandler(StatementHandler statementHandler) {
			this.statementHandler = statementHandler;
			return this;
		}
		public DataTypeHandlerBuilder resultSetHandler(ResultSetHandler resultSetHandler) {
			this.resultSetHandler = resultSetHandler;
			return this;
		}
		public DataTypeHandlerBuilder metaDataHandler(MetaDataHandler metaDataHandler) {
			this.metaDataHandler = metaDataHandler;
			return this;
		}
		
		public DataTypeHandler build() {
			return new DataTypeHandler(this.statementHandler, this.resultSetHandler, this.metaDataHandler);
		}
		
	}
}
