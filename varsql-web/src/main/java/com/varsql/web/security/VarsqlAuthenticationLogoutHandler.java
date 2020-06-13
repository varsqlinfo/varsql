package com.varsql.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import com.varsql.core.common.util.CommUtil;
import com.varsql.core.common.util.SecurityUtil;

/**
 * 
 * @FileName  : VarsqlAuthenticationLogoutHandler.java
 * @프로그램 설명 : log out handler
 * @Date      : 2019. 9. 21. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Component
public class VarsqlAuthenticationLogoutHandler implements LogoutHandler {
	private static final Logger logger = LoggerFactory.getLogger(VarsqlAuthenticationLogoutSuccessHandler.class);
	
	@Autowired
	private AuthDAO authDao;
	
	@Autowired
	PersistentTokenBasedRememberMeServices rememberMeService;
	
    public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
    	
    	if (authentication != null && authentication.getDetails() != null) {
			try {
				authDao.addLog(SecurityUtil.loginUser() , "logout", CommUtil.getClientPcInfo(request));
			} catch (Exception e) {
				logger.error("VarsqlAuthenticationLogoutSuccessHandler  onLogoutSuccess " , e.getMessage() , e);
			}
			rememberMeService.logout(request, response, authentication);
		}
    }
}