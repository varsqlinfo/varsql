package com.varsql.core.sql.executor.handler;

/**
 * -----------------------------------------------------------------------------
* @fileName		: AbstractSQLExecutorHandler.java
* @desc		: abstract sql executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public abstract class AbstractSQLExecutorHandler implements SQLExecutorHandler{
	long totalCount = 0;

	long failCount = 0;

	public long getTotalCount() {
		return totalCount;
	}

	public void addTotalCount() {
		++totalCount;
	}

	public long getFailCount() {
		return failCount;
	}

	public void addFailCount() {
		++this.failCount;
	}

}
