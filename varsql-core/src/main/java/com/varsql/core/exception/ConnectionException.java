package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

public class ConnectionException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public ConnectionException(String errorMessage) {
		super(VarsqlAppCode.EC_DB_CONNECTION, errorMessage);
	}

	public ConnectionException(Exception e) {
		super(VarsqlAppCode.EC_DB_CONNECTION, e.getMessage(), e);
	}

	public ConnectionException(String errorMessage, Exception e) {
		super(VarsqlAppCode.EC_DB_CONNECTION, e.getMessage(), e);
	}
}