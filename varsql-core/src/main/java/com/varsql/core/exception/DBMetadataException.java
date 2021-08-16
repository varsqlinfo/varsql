package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.vartech.common.constants.CodeEnumValue;

/**
 *
 * @FileName  : DBMetadataException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DBMetadataException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private CodeEnumValue errorCode;
	private String errorMessage;
	private String messageCode;

	@SuppressWarnings("unused")
	private DBMetadataException() {}

	/**
	 * @param s java.lang.String
	 */
	public DBMetadataException(String s) {
		this(s, null);
	}
	/**
	 * @param s java.lang.String
	 */
	public DBMetadataException(String s, Exception exeception) {
		this(VarsqlAppCode.COMM_RUNTIME_ERROR, exeception, s);
	}

	public DBMetadataException(CodeEnumValue errorCode,Exception exeception) {
		this(errorCode, exeception, null);
	}

	public DBMetadataException(CodeEnumValue errorCode, Exception exeception, String errorMessage) {
		this(errorCode, exeception, errorMessage , null);
	}
	public DBMetadataException(CodeEnumValue errorCode, Exception exeception, String errorMessage, String messageCode) {

		super(String.format("error code : %s %s %s", errorCode+"", (messageCode==null?"": "message code : " +  messageCode) , (errorMessage==null?"": "message :" +  errorMessage)), exeception);
		setErrorCode(errorCode);
		this.messageCode=messageCode;
		this.errorMessage = errorMessage;
	}

	public CodeEnumValue getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(CodeEnumValue errorCode) {
		this.errorCode = errorCode != null? errorCode : VarsqlAppCode.DB_META_ERROR;
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
