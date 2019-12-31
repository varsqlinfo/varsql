package com.varsql.core.exception;

public class ConnectionFactoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ConnectionFactoryException() {
		super();
	}
	
	public ConnectionFactoryException(Throwable cause) {
        super(cause);
    }

	/**
	 * @param s java.lang.String
	 */
	public ConnectionFactoryException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public ConnectionFactoryException(String s , Exception exeception) {
		super(s,exeception);
	}
}
