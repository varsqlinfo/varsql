package com.varsql.core.sql.executor.handler;

/**
 * -----------------------------------------------------------------------------
* @fileName		: UpdateExecutorHandler.java
* @desc		: Update executor handler
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public abstract class UpdateExecutorHandler extends AbstractSQLExecutorHandler{

	public abstract boolean handle(UpdateInfo sqlResultVO);
}
