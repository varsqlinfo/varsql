package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

/**
*
* @FileName  : FileNotFoundException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : file not found exception
*/
public class FileNotFoundException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public FileNotFoundException(String errorMessage) {
		super(VarsqlAppCode.COMM_FILE_EMPTY, errorMessage);
	}

	public FileNotFoundException(Exception e) {
		super(VarsqlAppCode.COMM_FILE_EMPTY, e.getMessage(), e);
	}

	public FileNotFoundException(String errorMessage, Exception e) {
		super(VarsqlAppCode.COMM_FILE_EMPTY, e.getMessage(), e);
	}

	public FileNotFoundException(VarsqlAppCode errorCode, Exception e) {
		super(errorCode, e);
	}
}
