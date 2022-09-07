package com.varsql.core.common.code;

import com.vartech.common.constants.CodeEnumValue;

import lombok.ToString;

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
	// java entity 변환 에러.
	,EC_BEAN_CONVERT(20000)

	// connection info dao not generate
	,EC_FACTORY_CONNECTION_ERROR(20001)
	// connection info dao not generate
	,EC_FACTORY_CONNECTION_INFO(20002)

	// driver not found
	,EC_DRIVER_NOT_FOUND(20002)

	// varsql configuration
	,EC_CONFIGURATION(20003)

	// template configuration error
	,EC_TEMPLATE_CONFIGURATION(20004)

	// method not found error
	,EC_METHOD_NOT_FOUND(20005)

	// permission
	,EC_PERMISSION(20006)

	// board error
	// board not found error
	,EC_BOARD_NOT_FOUND(21000)

	// board INVALID error
	,EC_BOARD_INVALID(21001)

	//board permission error
	,EC_BOARD_PERMISSION(21002)
	
	//scheduler error
	,EC_SCHEDULER(22000)



	// meta , ddl error
	,DB_META_ERROR(30000)
	,DB_META_DDL_ERROR(30001)
	,DB_BLOCKING_ERROR(30002)

	// 공통 코드 50000 ~60000
	// file upload empty
	,COMM_FILE_EMPTY(50000)
	// 유효 하지 않은 비밀번호
	,COMM_PASSWORD_NOT_VALID(50001)

	// file upload error
	,COMM_FILE_UPLOAD_ERROR(50002)

	// file download error
	,COMM_FILE_DOWNLOAD_ERROR(50003)

	// runtime error
	,COMM_RUNTIME_ERROR(50005)


	//db error 80000 ~90000
	// db pool error
	,EC_DB_POOL(80000)
	// db pool close error
	,EC_DB_POOL_CLOSE(80001)
	// db connection error;
	,EC_DB_CONNECTION(80002);

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
	
	@Override
	public String toString() {
		return this.name()+" ("+this.code+")";
	}
}
