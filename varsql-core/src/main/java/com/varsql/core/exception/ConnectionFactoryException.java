package com.varsql.core.exception;

/**
 *
 * @FileName  : ConnectionFactoryException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ConnectionFactoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorMessage;
	private String messageCode;

	/**
	 *
	 */
	public ConnectionFactoryException() {
		super();
	}

	public ConnectionFactoryException(Throwable cause) {
        super(cause);
    }

	public ConnectionFactoryException(int errorCode,Exception exeception) {
		this(errorCode,null, exeception);
	}

	public ConnectionFactoryException(int errorCode,String errorMessage) {
		this(errorCode, null, errorMessage, new Exception(errorMessage));
	}

	public ConnectionFactoryException(int errorCode, String messageCode,Exception exeception) {
		this(errorCode, messageCode , null, exeception);
	}
	public ConnectionFactoryException(int errorCode,String messageCode,	String errorMessage, Exception exeception) {
		super(errorMessage, exeception);
		this.errorCode=errorCode ;
		this.messageCode=messageCode;
		this.errorMessage = errorMessage;
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

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
