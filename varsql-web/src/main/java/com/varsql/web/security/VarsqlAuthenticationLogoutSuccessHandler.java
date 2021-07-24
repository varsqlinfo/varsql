package com.varsql.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : VarsqlAuthenticationLogoutSuccessHandler.java
 * @프로그램 설명 : 로그아웃 success handler
 * @Date      : 2019. 9. 21.
 * @작성자      : ytkim
 * @변경이력 :
 */
@Component
public class VarsqlAuthenticationLogoutSuccessHandler implements LogoutSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(VarsqlAuthenticationLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		String viewPage = request.getParameter("viewPage"); 
		
		if (authentication != null && authentication.getDetails() != null) {
			try {
				request.getSession().invalidate();
			} catch (Exception e) {
				logger.error("VarsqlAuthenticationLogoutSuccessHandler  onLogoutSuccess " , e.getMessage() , e);
			}
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		if(!StringUtils.isBlank(viewPage)) {
			response.sendRedirect(request.getContextPath() + viewPage);
		}else {
			response.sendRedirect(request.getContextPath());
		}

		
	}
}