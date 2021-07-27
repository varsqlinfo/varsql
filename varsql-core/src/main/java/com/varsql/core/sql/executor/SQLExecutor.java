package com.varsql.core.sql.executor;

import java.sql.SQLException;

import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;

/**
*
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SQLExecutor.java
* @DESC		: query executor
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
 DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 12. 11. 			ytkim			최초작성

*-----------------------------------------------------------------------------
*/
public interface SQLExecutor {

	public SQLExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException ;

	public SQLExecuteResult execute(SqlStatementInfo statementInfo, AbstractSQLExecutorHandler resultHandler) throws SQLException ;
}
