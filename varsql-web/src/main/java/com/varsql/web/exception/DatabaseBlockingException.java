package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : DatabaseBlockingException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : database blocking exception
*/
public class DatabaseBlockingException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseBlockingException(String errorMessage) {
		super(VarsqlAppCode.DB_BLOCKING_ERROR, errorMessage);
	}

}