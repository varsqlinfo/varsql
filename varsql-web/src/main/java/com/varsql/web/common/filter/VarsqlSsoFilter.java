package com.varsql.web.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.varsql.core.sso.SsoHandler;
import com.varsql.web.common.sso.SsoBeanFactory;
import com.varsql.web.common.sso.SsoComponent;

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
public class VarsqlSsoFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(VarsqlSsoFilter.class);

    private SsoBeanFactory ssoBeanFactory;

	private SsoComponent ssoComponent;

	private SsoHandler ssoHandler;

	public VarsqlSsoFilter(SsoBeanFactory ssoBeanFactory,SsoComponent ssoComponent) {
		this.ssoBeanFactory = ssoBeanFactory;
		this.ssoComponent = ssoComponent ;
		this.ssoHandler = this.ssoBeanFactory.getSsoBean();

		logger.info("VarsqlSsoFilter sso handler : {}", this.ssoHandler);
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

    	if(ssoHandler != null) {
    		ssoComponent.ssoLogin(ssoHandler.extractUserId(request, response));
    	}

        filterChain.doFilter(request,response);
    }

}