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

import com.varsql.core.common.util.SecurityUtil;

public class LanguageInterceptor implements HandlerInterceptor  {

	public static final String DEFAULT_PARAM_NAME = "locale";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		
		Locale locale;
		if(SecurityUtil.isAuthenticated()) {
			locale = SecurityUtil.loginInfo().getUserLocale();
		}else {
			locale = extractLocale(request);
		}
		
		if (locale != null && !locale.equals(request.getLocale())) {
			
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found.");
			}
			
			if(localeResolver.resolveLocale(request) != locale) {
				localeResolver.setLocale(request, response, locale);
			}
		}
		return true;
	}

	private Locale extractLocale(HttpServletRequest request) {
		String newLocale = extractLocaleString(request);
		if (StringUtils.hasText(newLocale)) {
			return StringUtils.parseLocaleString(newLocale);
		}
		return request.getLocale();
	}

	@SuppressWarnings({ "rawtypes" })
	private String extractLocaleString(HttpServletRequest request) {
		Map pathVariables = (Map) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String newLocale = null;
		
		if (MapUtils.isNotEmpty(pathVariables)) {
			newLocale = (String) pathVariables.get(DEFAULT_PARAM_NAME);
		}
		if (!StringUtils.hasText(newLocale)) {
			newLocale = request.getParameter(DEFAULT_PARAM_NAME);
		}
		return newLocale;
	}
}
