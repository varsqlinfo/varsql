package com.varsql.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfRequestMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {

    	String xRequestedWith = request.getHeader("X-Requested-With");

		if (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith)) {
			return true; 
		}else{
			return false;
		}
    }
}