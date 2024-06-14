package com.varsql.web.common.interceptor;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;

public class LanguageInterceptor implements HandlerInterceptor  {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		
		if("post".equalsIgnoreCase(request.getMethod())) {
			return true; 
		}
		
		Locale locale;
		if(SecurityUtil.isAuthenticated()) {
			locale = SecurityUtil.loginInfo().getUserLocale();
		}else {
			locale = LocaleConstants.parseLocaleString(extractLocaleString(request));
		}
		
		if(!locale.getLanguage().equals(request.getSession().getAttribute(HttpSessionConstants.USER_LOCALE))) {
			VarsqlUtils.changeLocale(request, response, locale);
		}
		
		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	private String extractLocaleString(HttpServletRequest request) {
		Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String newLocale = null;
		
		if (MapUtils.isNotEmpty(pathVariables)) {
			newLocale = (String) pathVariables.get(HttpParamConstants.LANG_LOCALE);
		}
		if (!StringUtils.hasText(newLocale)) {
			newLocale = request.getParameter(HttpParamConstants.LANG_LOCALE);
		}
		return newLocale != null ? newLocale : request.getLocale().getLanguage();
	}
}
