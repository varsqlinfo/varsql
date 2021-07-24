package com.varsql.web.security.rememberme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.web.app.admin.controller.AdminController;
import com.varsql.web.constants.SecurityConstants;
import com.vartech.common.utils.StringUtils;

public class RememberMeHttpServletRequestWapper extends HttpServletRequestWrapper {
	
	private final Logger logger = LoggerFactory.getLogger(RememberMeHttpServletRequestWapper.class);
	
	private String rememberMeCookieValue = null; 
	public RememberMeHttpServletRequestWapper(HttpServletRequest request) {
		super(request);
	}
	
	public RememberMeHttpServletRequestWapper(HttpServletRequest request, HttpServletResponse response) {
		super(request);
		
		Collection<String> cookies = response.getHeaders("Set-Cookie");
		
		for (String cookie : cookies) {
			if(cookie.startsWith(SecurityConstants.REMEMBERME_COOKIENAME)) {
				String val = getRememberCookieValue(cookie); 
				
				if(!StringUtils.isBlank(val)) {
					rememberMeCookieValue = val;
				}
			}
		}
	}
	
	private String getRememberCookieValue(String headerValue) {
		String[] cookieConfigValue = headerValue.split(";");
		
		if(cookieConfigValue.length > 0) {
			for (String value: cookieConfigValue) {
				if(value.startsWith(SecurityConstants.REMEMBERME_COOKIENAME)) {
					int idx = value.indexOf("=");
					return value.substring(idx+1 , value.length());
				}
			}
		}
		return null; 
	}
	
	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();
		
		logger.debug("RememberMeHttpServletRequestWapper getCookies rememberMeCookieValue : {}", rememberMeCookieValue);
		
		if(rememberMeCookieValue==null) return cookies;
		
		List<Cookie> reval = new ArrayList<Cookie>();
		
		for (Cookie cookie :  cookies) {
			if(SecurityConstants.REMEMBERME_COOKIENAME.equals(cookie.getName())) {
				if(!rememberMeCookieValue.equals(cookie.getValue())) {
					reval.add(new Cookie(SecurityConstants.REMEMBERME_COOKIENAME , rememberMeCookieValue));
				}else {
					return cookies; 
				}
			}else {
				reval.add(cookie);
			}
		}
		return reval.toArray(new Cookie[0]); 
	}
}
