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
public class FileUploadException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public FileUploadException(String errorMessage) {
		super(VarsqlAppCode.COMM_FILE_UPLOAD_ERROR, errorMessage);
	}

	public FileUploadException(Exception e) {
		super(VarsqlAppCode.COMM_FILE_UPLOAD_ERROR, e.getMessage(), e);
	}

	public FileUploadException(String errorMessage, Exception e) {
		super(VarsqlAppCode.COMM_FILE_UPLOAD_ERROR, e.getMessage(), e);
	}

	public FileUploadException(VarsqlAppCode errorCode, Exception e) {
		super(errorCode, e);
	}
}
