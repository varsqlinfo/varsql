package com.varsql.core.db.datatype.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.vartech.common.utils.StringUtils;

public interface StatementHandler {
	default void setParameter(PreparedStatement stmt, int parameterIndex, Object value) throws SQLException {
		stmt.setObject(parameterIndex, value);
	}

	default void setOutParameter(CallableStatement stmt, int parameterIndex, int sqlType) throws SQLException{
		setOutParameter(stmt, parameterIndex, sqlType, null);
	};
	
	default void setOutParameter(CallableStatement stmt, int parameterIndex, int sqlType, String typeName) throws SQLException{
		if(StringUtils.isBlank(typeName)) {
			stmt.registerOutParameter(parameterIndex, sqlType);
		}else {
			stmt.registerOutParameter(parameterIndex, sqlType, typeName);
		}
	};
}

