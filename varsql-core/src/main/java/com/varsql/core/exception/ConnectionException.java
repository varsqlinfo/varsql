package com.varsql.core.exception;

public class ConnectionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	
	public ConnectionException() {
		super();
	}
	
	public ConnectionException(Throwable cause) {
        super(cause);
    }

	/**
	 * @param s java.lang.String
	 */
	public ConnectionException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public ConnectionException(String s , Throwable exeception) {
		super(s,exeception);
	}
}