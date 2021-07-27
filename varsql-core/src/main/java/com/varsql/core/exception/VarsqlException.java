package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.vartech.common.constants.CodeEnumValue;

/**
 *
 * @FileName  : VarsqlException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorMessage;
	private String messageCode;

	@SuppressWarnings("unused")
	private VarsqlException() {}

	/**
	 * @param s java.lang.String
	 */
	public VarsqlException(String s) {
		this(s, null);
	}
	/**
	 * @param s java.lang.String
	 */
	public VarsqlException(String s, Exception exeception) {
		this(VarsqlAppCode.COMM_RUNTIME_ERROR, exeception, s);
	}

	public VarsqlException(CodeEnumValue errorCode,Exception exeception) {
		this(errorCode, exeception, null);
	}

	public VarsqlException(CodeEnumValue errorCode, Exception exeception, String errorMessage) {
		this(errorCode, exeception, errorMessage , null);
	}
	public VarsqlException(CodeEnumValue errorCode, Exception exeception, String errorMessage, String messageCode) {

		super(String.format("error code : %s %s %s", errorCode+"", (messageCode==null?"": "message code : " +  messageCode) , (errorMessage==null?"": "message :" +  errorMessage)), exeception);
		setErrorCode(errorCode);
		this.messageCode=messageCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(CodeEnumValue errorCode) {
		if(errorCode != null) {
			this.errorCode = errorCode.getCode();
		}

		this.errorCode = VarsqlAppCode.ERROR.getCode();
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
