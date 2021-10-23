package com.varsql.web.exception;

import java.io.IOException;
import java.sql.SQLException;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : VarsqlAppException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : varsql exception
*/
public class VarsqlAppException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public VarsqlAppException(String errorMessage) {
		super(VarsqlAppCode.COMM_RUNTIME_ERROR, errorMessage);
	}

	public VarsqlAppException(String errorMessage, SQLException e) {
		super(VarsqlAppCode.COMM_RUNTIME_ERROR, errorMessage, e);
	}

	public VarsqlAppException(String errorMessage, IOException e) {
		super(VarsqlAppCode.COMM_RUNTIME_ERROR, errorMessage, e);
	}

}