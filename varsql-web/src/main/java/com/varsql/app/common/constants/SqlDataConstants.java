package com.varsql.app.common.constants;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ResultConstants.java
* @DESC		: 어플리케이션 result 처리.  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface SqlDataConstants {
	
	// default select limit row count
	public int DEFAULT_LIMIT_ROW_COUNT =1000; 
	
	enum ERROR{
		SQL(10000)
		,CONNECTION(10001)// sql query error code 10000 번 부터 시작.
		,RESULT_CONVERT(10002) // sql result set 변환 에러. 
		;
		
		int code = -1; 
		ERROR(int pcode){
			this.code = pcode;
		}
		
		public int intVal(){
			return this.code; 
		}
		
		@Override
		public String toString() {
			return this.code+"";
		}
	}
	
	// sql data result view type
	enum VIEWTYPE{
		GRID("grid") 
		,MSG("msg");
		
		String val; 
		VIEWTYPE(String val){
			this.val = val;
		}
		
		public String val() {
			return this.val+"";
		}
		
		@Override
		public String toString() {
			return this.val+"";
		}
	}
}
