package com.varsql.web.exception;

/**
*
* @FileName  : DataDownloadException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : 데이터 다운로드 eror 
*/
public class DataDownloadException extends RuntimeException {

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
	public DataDownloadException() {
		super();
	}
	/**
	 * @param s java.lang.String
	 */
	public DataDownloadException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public DataDownloadException(String s , Exception exeception) {
		super(s,exeception);
	}
	
	
	public DataDownloadException(int errorCode,Exception exeception) {
		this(errorCode,null, exeception);
	}
	
	public DataDownloadException(int errorCode, String messageCode ,Exception exeception) {
		this(errorCode, messageCode , null, exeception);
	}
	public DataDownloadException(int errorCode,String messageCode,	String errorMessage, Exception exeception) {
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
