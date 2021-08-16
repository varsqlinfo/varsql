package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.vartech.common.constants.CodeEnumValue;

/**
*
* @FileName  : VarsqlTagException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : varsql jsp tag exception
*/
public class VarsqlTagException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private CodeEnumValue errorCode;
	private String errorMessage;
	private String messageCode;

	/**
	 *
	 */
	public VarsqlTagException() {
		super();
	}
	/**
	 * @param s java.lang.String
	 */
	public VarsqlTagException(String s) {
		super(s);
	}
	/**
	 * @param s java.lang.String
	 */
	public VarsqlTagException(String s , Exception exeception) {
		super(s,exeception);
	}


	public VarsqlTagException(CodeEnumValue errorCode,Exception exeception) {
		this(errorCode,null, exeception);
	}

	public VarsqlTagException(CodeEnumValue errorCode, String messageCode ,Exception exeception) {
		this(errorCode, messageCode , null, exeception);
	}
	public VarsqlTagException(CodeEnumValue errorCode,String messageCode,	String errorMessage, Exception exeception) {
		super(errorMessage, exeception);
		this.errorCode=errorCode ;
		this.messageCode=messageCode;
		this.errorMessage = errorMessage;
	}

	public CodeEnumValue getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(CodeEnumValue errorCode) {
		this.errorCode = errorCode != null? errorCode : VarsqlAppCode.COMM_RUNTIME_ERROR;
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
