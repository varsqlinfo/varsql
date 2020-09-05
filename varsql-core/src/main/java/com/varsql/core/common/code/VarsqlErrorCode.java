package com.varsql.core.common.code;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlErrorCode.java
* @DESC		: varsql error code 처리  90000 번 부터 시작 할것.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public enum VarsqlErrorCode {
	SQL(10000)
	,SQL_CONNECTION(10001)// sql query error code 10000 번 부터 시작.
	,SQL_RESULT_CONVERT(10002) // sql result set 변환 에러.
	,DB_POOL_ERROR(80000)
	,DB_POOL_CLOSE(80001)

	,PASSWORD_NOT_VALID(90000);

	int code = -1;
	VarsqlErrorCode(int pcode){
		this.code = pcode;
	}

	public int code(){
		return this.code;
	}

	@Override
	public String toString() {
		return this.code+"";
	}


}
