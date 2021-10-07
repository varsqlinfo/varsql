package com.varsql.core.sso;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*
* @FileName  : AbstractSsoHandler.java
* @프로그램 설명 : sso handler 추상화 클래스
* @Date      : 2019. 11. 26.
* @작성자      : ytkim
* @변경이력 :
*/
public abstract class AbstractSsoHandler implements SsoHandler {

	@Override
	public boolean beforeSsoHandler(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	@Override
	public Map<String, Object> customUserInfo(HttpServletRequest request, HttpServletResponse response,	@SuppressWarnings("rawtypes") Map userInfo) {
		return null;
	}

	@Override
	public boolean afterSsoHandler(HttpServletRequest request, HttpServletResponse response, String ssoId) {
		return true;
	}

	@Override
	public List<String> userAuthorities(String userId, HttpServletRequest request, HttpServletResponse response) {
		return Collections.EMPTY_LIST;
	}
}
