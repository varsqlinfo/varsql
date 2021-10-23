package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

public class ConfigurationLoadException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public ConfigurationLoadException(String errorMessage) {
		super(VarsqlAppCode.EC_CONFIGURATION, errorMessage);
	}

	public ConfigurationLoadException(Exception e) {
		super(VarsqlAppCode.EC_CONFIGURATION, e.getMessage(), e);
	}
}