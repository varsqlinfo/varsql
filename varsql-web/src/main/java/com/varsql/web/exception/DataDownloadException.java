package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : DataDownloadException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : 데이터 다운로드 eror
*/
public class DataDownloadException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public DataDownloadException(String errorMessage) {
		super(VarsqlAppCode.COMM_FILE_DOWNLOAD_ERROR, errorMessage);
	}

	public DataDownloadException(VarsqlAppCode resultCode, String message, VarsqlAppException varsqlAppException) {
		super(resultCode, message, varsqlAppException);
	}

}