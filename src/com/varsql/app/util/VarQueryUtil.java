package com.varsql.app.util;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarQueryUtil.java
* @DESC		: mybatis 쿼리 util
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class VarQueryUtil {
	private VarQueryUtil() {}
	
	public static boolean isNumber(Object obj){
		try {
	        Integer.parseInt(obj.toString());
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
}
