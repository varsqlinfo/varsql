package com.varsql.core.sso;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vartech.common.utils.StringUtils;

/**
*
* @FileName  : AbstractSsoHandler.java
* @프로그램 설명 : sso handler 추상화 클래스
* @Date      : 2019. 11. 26.
* @작성자      : ytkim
* @변경이력 :
*/
public abstract class AbstractSsoHandler implements SsoHandler {

	final static String REDIRECT_URL ="redirectURL";
	
	final static String FAILURE_REDIRECT_URL ="failureRedirectURL"; 
	
	/**
	 * login page url
	 */
	@Override
	public String loginURL(HttpServletRequest request, HttpServletResponse response) {
		return "";
	}
	
	// sso 체크. 
	@Override
	public boolean beforeSso(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}
	
	@Override
	public Map<String, Object> getAdditionalUserInfo(HttpServletRequest request, HttpServletResponse response,	@SuppressWarnings("rawtypes") Map userInfo) {
		return null;
	}

	@Override
	public boolean afterSso(HttpServletRequest request, HttpServletResponse response, Object auth) {
		return true;
	}
	
	@Override
	public String failureRedirectURL(HttpServletRequest request, HttpServletResponse response, Object auth) {
		String redirectURL = request.getParameter(FAILURE_REDIRECT_URL); 
		if(!StringUtils.isBlank(redirectURL)) {
			return redirectURL;
		}
		
		return null;
	}

	@Override
	public List<String> getAdditionalAuthorities(String userId, HttpServletRequest request, HttpServletResponse response) {
		return Collections.emptyList();
	}
	
	@Override
	public String successRedirectURL(HttpServletRequest request, HttpServletResponse response, Object auth) {
		String redirectURL = request.getParameter(REDIRECT_URL); 
		if(!StringUtils.isBlank(redirectURL)) {
			return redirectURL;
		}
		
		return null;
	}
}
