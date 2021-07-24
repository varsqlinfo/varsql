package com.varsql.web.util;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.varsql.web.constants.ResourceConfigConstants;

/**
 * -----------------------------------------------------------------------------
* @fileName		: PasswordUtils.java
* @desc		: 패스워드 utils 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class PasswordUtils {
	
	
	public static String encode (String pw) {
		return ((PasswordEncoder)VarsqlBeanUtils.getStringBean(ResourceConfigConstants.APP_PASSWORD_ENCODER)).encode(pw);
	}
	
	public static String decode (String pw) {
		return pw; 
	}
}
