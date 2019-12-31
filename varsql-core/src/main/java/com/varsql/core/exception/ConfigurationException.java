package com.varsql.core.exception;

public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ConfigurationException() {
		super();
	}
	
	public ConfigurationException(Throwable cause) {
        super(cause);
    }

	/**
	 * @param s java.lang.String
	 */
	public ConfigurationException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public ConfigurationException(String s , Exception exeception) {
		super(s,exeception);
	}
}
