package com.varsql.core.sql.executor;

import java.sql.SQLException;

import com.varsql.core.db.valueobject.SqlStatementInfo;

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
public interface Executor {

	public SQLExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException;
}
