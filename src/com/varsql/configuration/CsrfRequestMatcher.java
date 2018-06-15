package com.varsql.configuration;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfRequestMatcher implements RequestMatcher {

    // Always allow the HTTP GET method
    private Pattern allowedMethods = Pattern.compile("^GET$");

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