package com.varsql.core.sql.executor.handler;

import com.vartech.common.io.writer.AbstractWriter;

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

	private AbstractWriter writer;

	public AbstractSQLExecutorHandler() {

	}

	public AbstractSQLExecutorHandler(AbstractWriter writer) {
		this.setWriter(writer);
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void addTotalCount() {
		++totalCount;
	}

	public AbstractWriter getWriter() {
		return writer;
	}

	public void setWriter(AbstractWriter writer) {
		this.writer = writer;
	}

	public long getFailCount() {
		return failCount;
	}

	public void addFailCount() {
		++this.failCount;
	}



}
