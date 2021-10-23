package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

/**
 *
 * @FileName  : ConnectionFactoryException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ConnectionFactoryException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;

	public ConnectionFactoryException(String errorMessage) {
		super(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR, errorMessage);
	}

	public ConnectionFactoryException(Exception e) {
		super(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR, e.getMessage(), e);
	}

	public ConnectionFactoryException(String errorMessage, Exception e) {
		super(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR, errorMessage, e);
	}

	public ConnectionFactoryException(VarsqlAppCode errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	public ConnectionFactoryException(VarsqlAppCode errorCode, String errorMessage, Exception e) {
		super(errorCode, errorMessage, e);
	}
}