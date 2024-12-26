package com.varsql.web.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlSsoFilter.java
* @desc		: VarsqlSsoFilter
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2019. 1. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class AppInitFilter extends OncePerRequestFilter {
	
	private final Logger logger = LoggerFactory.getLogger(AppInitFilter.class);
	
	private OrRequestMatcher setupMatcher = new OrRequestMatcher(
		 new AntPathRequestMatcher("/setup")
		, new AntPathRequestMatcher("/i18nMessage")
		, new AntPathRequestMatcher("/setup/**")
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.info("request uri {}", request.getRequestURI() );

		if (!setupMatcher.matcher(request).isMatch()) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			try {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/setup");
				return;
			} catch (Exception e) {
				throw e;
			}
		}
		filterChain.doFilter(request, response);
	}

}