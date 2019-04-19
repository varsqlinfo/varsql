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
public interface ResultConstants {
	
	public String MESSAGE ="msg";
	public String CODE ="code";
	public String RESULT_ITEMS ="items";
	public String PAGING ="paging";
	public String RESULT ="result";
	
	enum CODE_VAL{
		SUCCESS(200) 
		,ERROR(500) 
		,NOT_FOUND(400)  
		,SQL_ERROR(10000)	// sql query error code 10000 번 부터 시작. 
		;
		
		int code = -1; 
		CODE_VAL(int pcode){
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
}
