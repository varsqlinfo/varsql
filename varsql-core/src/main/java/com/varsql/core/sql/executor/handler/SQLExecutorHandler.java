package com.varsql.core.sql.executor.handler;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SQLExecutorHandler.java
* @desc		:  sql executor
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface SQLExecutorHandler {
	default Object getResult() {
		return null;
	}
}
