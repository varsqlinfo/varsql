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
public class ResultSetConvertException extends VarsqlRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private SqlSourceResultVO  ssrv;


	public ResultSetConvertException(String errorMessage) {
		super(VarsqlAppCode.EC_SQL_RESULT_CONVERT, errorMessage);
	}

	public ResultSetConvertException(Exception e) {
		super(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e.getMessage(), e);
	}

	public ResultSetConvertException(String errorMessage, Exception e) {
		super(VarsqlAppCode.EC_SQL_RESULT_CONVERT, e.getMessage(), e);
	}

	public ResultSetConvertException(CodeEnumValue errorCode, Exception exeception) {
		super(errorCode, exeception);
	}

	public ResultSetConvertException(CodeEnumValue errorCode, Exception exeception , SqlSourceResultVO ssrv) {
		super(errorCode, exeception);
		this.ssrv = ssrv;
	}

	public SqlSourceResultVO getSsrv() {
		return ssrv;
	}

	public void setSsrv(SqlSourceResultVO ssrv) {
		this.ssrv = ssrv;
	}



}
