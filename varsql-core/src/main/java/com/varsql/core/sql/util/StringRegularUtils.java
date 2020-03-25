package com.varsql.core.sql.util;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: StringRegularUtils.java
* @DESC		: string regular expression 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 3. 17. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class StringRegularUtils {
	private static String BLANK_SPACE_REMOVE = "(?m)^\\s*\\r?\\n|\\r?\\n\\s*(?!.*\\r?\\n)";

	private static String LTRIM = "^\\s+";

	private static String RTRIM = "\\s+$";
	
	private static String ALLTRIM = "\\s";
	
	/**
	 * 
	 * @Method Name  : removeBlank
	 * @Method 설명 : text remove blank space
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 17. 
	 * @변경이력  :
	 * @param str
	 * @return
	 */
	public static String removeBlank(String str) {
		return removeBlank(str, System.lineSeparator());
	}
	
	/**
	 * 
	 * @Method Name  : removeBlank
	 * @Method 설명 :text remove blank space
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 17. 
	 * @변경이력  :
	 * @param str
	 * @param multiSpaceLineSeparator
	 * @return
	 */
	public static String removeBlank(String str, String multiSpaceLineSeparator) {
		return trim(str.replaceAll(BLANK_SPACE_REMOVE, multiSpaceLineSeparator));
	}
	
	/**
	 * 
	 * @Method Name  : ltrim
	 * @Method 설명 : left trim
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 17. 
	 * @변경이력  :
	 * @param str
	 * @return
	 */
	public static String ltrim(String str) {
		return str.replaceAll(LTRIM, "");
	}
	
	/**
	 * 
	 * @Method Name  : rtrim
	 * @Method 설명 : right trim
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 17. 
	 * @변경이력  :
	 * @param str
	 * @return
	 */
	public static String rtrim(String str) {
		return str.replaceAll(RTRIM, "");
	}
	
	/**
	 * 
	 * @Method Name  : trim
	 * @Method 설명 : left , right trim
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 17. 
	 * @변경이력  :
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return rtrim(ltrim(str));
	}
	
	public static String allTrim(String str) {
		return str.replaceAll(ALLTRIM, "");
	}
}
