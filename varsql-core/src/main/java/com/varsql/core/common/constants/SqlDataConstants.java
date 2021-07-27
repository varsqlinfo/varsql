package com.varsql.core.common.constants;

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

	enum RESULT_TYPE{
		SUCCESS("success")
		,FAIL("fail");

		String val;
		RESULT_TYPE(String val){
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
