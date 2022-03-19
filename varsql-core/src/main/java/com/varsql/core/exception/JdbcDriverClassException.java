package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

/**
*
* @FileName  : FileUploadException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : file not found exception
*/
public class JdbcDriverClassException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public JdbcDriverClassException(String errorMessage) {
		super(VarsqlAppCode.EC_DRIVER_NOT_FOUND, errorMessage);
	}

	public JdbcDriverClassException(Exception e) {
		super(VarsqlAppCode.EC_DRIVER_NOT_FOUND, e.getMessage(), e);
	}

	public JdbcDriverClassException(String errorMessage, Exception e) {
		super(VarsqlAppCode.EC_DRIVER_NOT_FOUND, e.getMessage(), e);
	}

	public JdbcDriverClassException(VarsqlAppCode errorCode, Exception e) {
		super(errorCode, e);
	}
}
