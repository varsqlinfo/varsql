package com.varsql.core.sql.executor.handler;

import com.vartech.common.io.writer.AbstractWriter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SelectExecutorHandler.java
* @desc		: select executor handler
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public abstract class SelectExecutorHandler extends AbstractSQLExecutorHandler{
	private AbstractWriter writer;

	public SelectExecutorHandler(AbstractWriter writer) {
		this.writer =writer;
	}

	public AbstractWriter getWriter() {
		return writer;
	}

	public abstract boolean handle(SelectInfo sqlResultVO);
}
