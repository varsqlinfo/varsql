package com.varsql.web.exception;

import java.sql.SQLException;

import com.varsql.core.sql.builder.SqlSourceResultVO;

/**
*
* @FileName  : VarsqlResultConvertException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : varsql result convert exception
*/
public class VarsqlResultConvertException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	private SqlSourceResultVO  ssrv;
	
	/**
	 * 
	 */
	public VarsqlResultConvertException() {
		super();
	}
	
	public VarsqlResultConvertException(int errorCode, SqlSourceResultVO ssrv, SQLException exeception) {
		super(exeception);
		this.errorCode=errorCode ;
		this.ssrv = ssrv;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public SqlSourceResultVO getSsrv() {
		return ssrv;
	}

	public void setSsrv(SqlSourceResultVO ssrv) {
		this.ssrv = ssrv;
	}
	
	

}
