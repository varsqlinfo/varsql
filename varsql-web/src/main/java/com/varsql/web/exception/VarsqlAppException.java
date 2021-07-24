package com.varsql.web.exception;

/**
*
* @FileName  : VarsqlAppException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : varsql exception
*/
public class VarsqlAppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	private String errorMessage;
	private String messageCode;
	
	/**
	 * 
	 */
	public VarsqlAppException() {
		super();
	}
	/**
	 * @param s java.lang.String
	 */
	public VarsqlAppException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public VarsqlAppException(String s , Exception exeception) {
		super(s,exeception);
	}
	
	
	public VarsqlAppException(int errorCode,Exception exeception) {
		this(errorCode,null, exeception);
	}
	
	public VarsqlAppException(int errorCode, String messageCode ,Exception exeception) {
		this(errorCode, messageCode , null, exeception);
	}
	public VarsqlAppException(int errorCode,String messageCode,	String errorMessage, Exception exeception) {
		super(errorMessage, exeception);
		this.errorCode=errorCode ;
		this.messageCode=messageCode;
		this.errorMessage = errorMessage;
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
