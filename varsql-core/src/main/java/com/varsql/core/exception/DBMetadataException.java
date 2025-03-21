package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

/**
 *
 * @FileName  : DBMetadataException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DBMetadataException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public DBMetadataException(String errorMessage) {
		super(VarsqlAppCode.DB_META_ERROR, errorMessage);
	}

	public DBMetadataException(Throwable e) {
		super(VarsqlAppCode.DB_META_ERROR, e.getMessage(), e);
	}

	public DBMetadataException(String errorMessage, Throwable e) {
		super(VarsqlAppCode.DB_META_ERROR, errorMessage, e);
	}

	public DBMetadataException(VarsqlAppCode errorCode, Throwable e) {
		super(errorCode, e);
	}
}