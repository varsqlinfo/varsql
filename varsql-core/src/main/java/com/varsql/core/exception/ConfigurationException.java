package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

public class ConfigurationException extends VarsqlException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String errorMessage) {
		super(VarsqlAppCode.EC_CONFIGURATION, errorMessage);
	}

	public ConfigurationException(Exception e) {
		super(VarsqlAppCode.EC_CONFIGURATION, e.getMessage(), e);
	}
}