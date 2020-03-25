package com.varsql.core.pattern.convert;

public class ConverterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConverterException(String message) {
		super(message);
	}

	public ConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConverterException(Throwable cause) {
		super(cause);
	}
}