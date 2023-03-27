package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : DatabaseInvalidException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : database invalid exception
*/
public class DatabaseInvalidException  extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseInvalidException(String errorMessage) {
		super(VarsqlAppCode.INVALID_DATABASE, errorMessage);
	}

}