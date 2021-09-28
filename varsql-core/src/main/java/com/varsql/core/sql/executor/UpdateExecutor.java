package com.varsql.core.sql.executor;

import java.io.File;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.data.importdata.handler.AbstractImportDataHandler;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;

/**
 * -----------------------------------------------------------------------------
* @fileName		: UpdateExecutor.java
* @desc		: update executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public abstract class UpdateExecutor implements SQLExecutor{

	private int batchCount = 1000;

	public int getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}


	@Override
	public SQLExecuteResult execute(SqlStatementInfo statementInfo) throws SQLException {
		return execute(statementInfo , null);
	}

	@Override
	public SQLExecuteResult execute(SqlStatementInfo statementInfo, AbstractSQLExecutorHandler resultHandler) throws SQLException {
		return null;
	}
}
