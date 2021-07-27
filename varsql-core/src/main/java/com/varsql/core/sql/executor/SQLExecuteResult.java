package com.varsql.core.sql.executor;

import com.varsql.core.common.code.VarsqlAppCode;

import lombok.Getter;
import lombok.Setter;

/**
*
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: Executor.java
* @DESC		: query executor
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
 DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 12. 11. 			ytkim			최초작성

*-----------------------------------------------------------------------------
*/
@Getter
@Setter
public class SQLExecuteResult {
	private long startTime;
	private long endTime;
	private String message;
	private VarsqlAppCode resultCode;
	private long totalCount;
	private Object result;

	@SuppressWarnings("unchecked")
	public <T>T getResult() {
		return (T)result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
