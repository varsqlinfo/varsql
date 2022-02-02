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
	public static String progressKey(String requid) {
		return requid+"_progress";
	}
}
