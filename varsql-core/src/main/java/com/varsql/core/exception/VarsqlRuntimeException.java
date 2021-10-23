package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.vartech.common.constants.CodeEnumValue;

/**
 *
 * @FileName  : VarsqlRuntimeException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private CodeEnumValue errorCode;
	private String errorMessage;

	public VarsqlRuntimeException(CodeEnumValue errorCode) {
		this(errorCode, "");
	}

	public VarsqlRuntimeException(CodeEnumValue errorCode, String errorMessage) {
		this(errorCode, errorMessage, null);
	}

	public VarsqlRuntimeException(CodeEnumValue errorCode, Exception exeception) {
		this(errorCode, exeception.getMessage(), exeception);
	}

	public VarsqlRuntimeException(CodeEnumValue errorCode, String errorMessage, Exception exeception) {
		super(String.format("error code : %s %s", errorCode+"",  (errorMessage==null?"": "message :" +  errorMessage)), exeception);
		setErrorCode(errorCode);
		this.errorMessage = errorMessage;
	}

	public CodeEnumValue getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(CodeEnumValue errorCode) {
		this.errorCode = errorCode != null? errorCode : VarsqlAppCode.COMM_RUNTIME_ERROR;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
