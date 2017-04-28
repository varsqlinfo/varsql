package com.varsql.web.common.interceptor;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

public class LanguageInterceptor extends HandlerInterceptorAdapter {

	public static final String DEFAULT_PARAM_NAME = "locale";
	private String paramName = DEFAULT_PARAM_NAME;

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamName() {
		return this.paramName;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		
		Locale locale = extractLocale(request);
		if (locale != null && !locale.equals(request.getLocale())) {
			LocaleResolver localeResolver = RequestContextUtils
					.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found.");
			}
			localeResolver.setLocale(request, response, locale);
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
			newLocale = (String) pathVariables.get(getParamName());
		}
		if (!StringUtils.hasText(newLocale)) {
			newLocale = request.getParameter(getParamName());
		}
		return newLocale;
	}
}
