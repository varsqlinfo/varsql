package com.varsql.web.exception;

import java.io.IOException;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : VarsqlTagException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : varsql jsp tag exception
*/
public class VarsqlTagException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public VarsqlTagException(String errorMessage) {
		super(VarsqlAppCode.COMM_RUNTIME_ERROR, errorMessage);
	}

	public VarsqlTagException(String errorMessage, IOException e) {
		super(VarsqlAppCode.COMM_RUNTIME_ERROR, errorMessage, e);
	}
}