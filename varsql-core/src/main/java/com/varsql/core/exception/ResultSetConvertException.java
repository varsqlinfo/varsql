package com.varsql.core.exception;

import java.sql.SQLException;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.vartech.common.constants.CodeEnumValue;

/**
*
* @FileName  : VarsqlResultConvertException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : varsql result convert exception
*/
public class ResultSetConvertException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;
	private SqlSourceResultVO  ssrv;

	/**
	 *
	 */
	public ResultSetConvertException() {
		super();
	}

	public ResultSetConvertException(CodeEnumValue errorCode, SQLException exeception) {
		this(errorCode, exeception , null);
	}

	public ResultSetConvertException(CodeEnumValue errorCode, SQLException exeception , SqlSourceResultVO ssrv) {
		super(exeception);
		setErrorCode(errorCode);
		this.ssrv = ssrv;
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

	public SqlSourceResultVO getSsrv() {
		return ssrv;
	}

	public void setSsrv(SqlSourceResultVO ssrv) {
		this.ssrv = ssrv;
	}



}
