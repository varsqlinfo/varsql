package com.varsql.web.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordUtils {
	
	
	public static String encode (String pw) {
		return ((PasswordEncoder)BeanUtils.getBean("varsqlPasswordEncoder")).encode(pw);
	}
	
	public static String decode (String pw) {
		return pw; 
	}
}
