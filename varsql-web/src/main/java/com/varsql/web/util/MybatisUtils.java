package com.varsql.web.util;

import com.varsql.core.common.util.SecurityUtil;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: MybatisUtils.java
* @DESC		: mybatis method util
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 9. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class MybatisUtils {
	private MybatisUtils() {}
	
	/**
	 * 
	 * @Method Name  : isAdmin
	 * @Method 설명 : 관리자 여부. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @return
	 */
	public static boolean isAdmin() {
		return SecurityUtil.isAdmin();
	}
	
	/**
	 * 
	 * @Method Name  : isNotAdmin
	 * @Method 설명 : 관리자 아닌지 여부. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @return
	 */
	public static boolean isNotAdmin() {
		return !SecurityUtil.isAdmin();
	} 
	
	public static boolean isNumber(Object obj){
		try {
	        Integer.parseInt(obj.toString());
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
}
