package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.vartech.common.constants.CodeEnumValue;

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

	private CodeEnumValue errorCode;
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
	public DataDownloadException(String s, Exception exeception) {
		super(s,exeception);
	}


	public DataDownloadException(CodeEnumValue errorCode, Exception exeception) {
		this(errorCode,null, exeception);
	}

	public DataDownloadException(CodeEnumValue errorCode, String messageCode, Exception exeception) {
		this(errorCode, messageCode , null, exeception);
	}
	public DataDownloadException(CodeEnumValue errorCode, String messageCode, String errorMessage, Exception exeception) {
		super(errorMessage, exeception);
		this.errorCode=errorCode ;
		this.messageCode=messageCode;
		this.errorMessage = errorMessage;
	}

	public CodeEnumValue getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(CodeEnumValue errorCode) {
		this.errorCode = errorCode != null? errorCode : VarsqlAppCode.COMM_FILE_DOWNLOAD_ERROR;
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
