package com.varsql.core.exception;

public class ConfigurationLoadException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ConfigurationLoadException() {
		super();
	}
	
	public ConfigurationLoadException(Throwable cause) {
        super(cause);
    }

	/**
	 * @param s java.lang.String
	 */
	public ConfigurationLoadException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public ConfigurationLoadException(String s , Exception exeception) {
		super(s,exeception);
	}
}
