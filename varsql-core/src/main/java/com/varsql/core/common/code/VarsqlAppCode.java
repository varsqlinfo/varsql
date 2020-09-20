package com.varsql.core.common.code;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlAppCode.java
* @DESC		: varsql app code
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 18. 			ytkim			최초작성
 EC  = error code
 RC = response result code
 COMM = common code

*-----------------------------------------------------------------------------
 */
public enum VarsqlAppCode {
	// 공통 코드 10000 ~ 49999
	EC_SQL(10000)
	,EC_SQL_CONNECTION(10001)// sql query error code 10000 번 부터 시작.
	,EC_SQL_RESULT_CONVERT(10002) // sql result set 변환 에러.

	// 공통 코드 50000 ~60000
	,COMM_FILE_EMPTY(50000) // file upload empty
	,COMM_PASSWORD_NOT_VALID(50001) // 유효 하지 않은 비밀번호


	//db error 80000 ~90000
	,EC_DB_POOL_ERROR(80000) // db pool error
	,EC_DB_POOL_CLOSE(80001);



	int code = -1;
	VarsqlAppCode(int pcode){
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
