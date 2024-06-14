package com.varsql.web.constants;

/**
 * -----------------------------------------------------------------------------
* @fileName : HttpSessionConstants.java
* @desc		: session attribute  
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2022. 2. 2. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface HttpSessionConstants {
	
	public String USER_SCREEN = "varsql.user.screen";
	public String USER_LOCALE = "varsql.current_user.locale";
	
	public static String progressKey(String progressUid) {
		return progressUid+"_progress";
	}
}
