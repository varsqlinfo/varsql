package com.varsql.core.common.code;

import com.vartech.common.constants.CodeEnumValue;

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
public enum VarsqlAppCode implements CodeEnumValue {

	SUCCESS(200)  //success code

	, ERROR(1000)  // application error
	, INVALID_DATABASE(2000)  // 유효하지 않은 DB 요청 에러

	// 공통 코드 10000 ~ 49999
	,EC_SQL(10000)
	,EC_SQL_CONNECTION(10001)// sql query error code 10000 번 부터 시작.
	,EC_SQL_RESULT_CONVERT(10002) // sql result set 변환 에러.
	,EC_SQL_EXECUTOR(10003) // sql result set 변환 에러.

	// java error
	,EC_BEAN_CONVERT(20000) // java entity 변환 에러.
	,EC_FACTORY_CONNECTION_INFO(20001) // connection info dao not generate

	, DRIVER_NOT_FOUND(20002)	// driver not found

	// meta , ddl error
	,DB_META_ERROR(30000)
	,DB_META_DDL_ERROR(30001)

	// 공통 코드 50000 ~60000
	,COMM_FILE_EMPTY(50000) // file upload empty
	,COMM_PASSWORD_NOT_VALID(50001) // 유효 하지 않은 비밀번호
	,COMM_FILE_UPLOAD_ERROR(50002) // file upload error
	,COMM_FILE_DOWNLOAD_ERROR(50003) // file download error

	,COMM_RUNTIME_ERROR(50005) // runtime error


	//db error 80000 ~90000
	,EC_DB_POOL_ERROR(80000) // db pool error
	,EC_DB_POOL_CLOSE(80001);

	private int code = -1;
	private String message;

	VarsqlAppCode(int pcode){
		this(pcode , null);
	}

	VarsqlAppCode(int pcode, String message){
		this.code = pcode;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.name()+" ("+this.code+")";
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String getDesc() {
		return this.message;
	}

	public static VarsqlAppCode valueOf(int val) {
		for (VarsqlAppCode code : values()) {
			if(code.code == val ) {
				return code;
			}
		}

		return VarsqlAppCode.ERROR;
	}

}
