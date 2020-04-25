package com.varsql.web.util;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: CheckUtils.java
* @DESC		: check util
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 10. 31. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class CheckUtils {
	
	private CheckUtils(){}
	
	/**
	 * 
	 * @Method Name  : isNull
	 * @Method 설명 : null check
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}
	
	public static boolean isNotNull(Object obj) {
		return obj != null;
	}
	
	/**
	 * 
	 * @Method Name  : isEmpty
	 * @Method 설명 : 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if(obj == null) {
			return true; 
		}else if(obj instanceof String) {
			return StringUtils.isEmpty(obj.toString());
		}else if (obj instanceof Collection) {
			return CollectionUtils.isEmpty((Collection)obj);
		}else {
			return ObjectUtils.isEmpty(obj);
		}
	}
	
	public static boolean isNumber(Object obj){
		try {
	        Integer.parseInt(obj.toString());
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static String getValidFileName(String fileName) {
		fileName = fileName.replace("../", "");
		fileName = fileName.replace("..\\", "");
		
		return fileName; 
	}
	
	/**
	 * 
	 * @Method Name  : getValidFilePathName
	 * @Method 설명 : 파일 경로 체크. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1. 
	 * @변경이력  :
	 * @param filePathName
	 * @return
	 */
	public static String getValidFilePathName(String filePathName) {
		filePathName = filePathName.replace("../", "");
		filePathName = filePathName.replace("..\\", "");
		
		return filePathName; 
	}
	
}
