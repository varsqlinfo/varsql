package com.varsql.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.vartech.common.utils.CommUtils;
import com.varsql.core.common.util.SecurityUtil;

/**
 * log out handler
* 
* @fileName	: VarsqlAuthenticationLogoutHandler.java
* @author	: ytkim
 */
@Component
public class VarsqlAuthenticationLogoutHandler implements LogoutHandler {
	private final Logger logger = LoggerFactory.getLogger(VarsqlAuthenticationLogoutSuccessHandler.class);

	@Autowired
	private SecurityLogService securityLogService;

    public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {

    	if (authentication != null && authentication.getDetails() != null) {
			try {
				securityLogService.addLog(SecurityUtil.loginInfo(), "logout", CommUtils.getClientPcInfo(request));
			} catch (Exception e) {
				logger.error("logout " , e.getMessage() , e);
			}
		}
    }
}