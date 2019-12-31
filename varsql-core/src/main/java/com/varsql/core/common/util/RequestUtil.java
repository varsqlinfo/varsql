package com.varsql.core.common.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
	public static boolean isAjaxRequest(HttpServletRequest request){
		String headerInfo = request.getHeader("X-Requested-With");
		
		if("XMLHttpRequest".equals(headerInfo)){
			return true;
		}else{
			return false; 
		}
	}
}
