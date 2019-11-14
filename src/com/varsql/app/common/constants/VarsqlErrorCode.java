package com.varsql.app.common.constants;

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
	
	PASSWORD_NOT_VALID(90000);
	
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
