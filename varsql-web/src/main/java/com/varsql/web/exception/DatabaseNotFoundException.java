package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : BoardNotFoundException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : board not found exception
*/
public class DatabaseNotFoundException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseNotFoundException(String errorMessage) {
		super(VarsqlAppCode.EC_DB_NOT_FOUND, errorMessage);
	}

}
