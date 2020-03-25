package com.varsql.core.sql.mapping;

public class ParameterParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParameterParseException(String message) {
		super(message);
	}

	public ParameterParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterParseException(Throwable cause) {
		super(cause);
	}
}