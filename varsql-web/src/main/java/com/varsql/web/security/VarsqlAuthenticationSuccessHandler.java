package com.varsql.web.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.User;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.CommUtil;
import com.varsql.core.common.util.SecurityUtil;

/**
 * 
 * @FileName  : VarsqlAuthenticationSuccessHandler.java
 * @프로그램 설명 : 로그인 성공시
 * @Date      : 2019. 6. 14. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Component
public class VarsqlAuthenticationSuccessHandler implements	AuthenticationSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(VarsqlAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	@Qualifier("authDao")
	private AuthDAO authDao;

	public VarsqlAuthenticationSuccessHandler() {
		super();
	}

	public void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws IOException {
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws IOException {
		User userInfo = SecurityUtil.loginUser();
		final String targetUrl = determineTargetUrl(request ,response, userInfo, authentication);
		
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to "
					+ targetUrl);
			return;
		}
		
		authDao.addLog(userInfo , "login", CommUtil.getClientPcInfo(request));

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,User userInfo , final Authentication authentication) {
		
		AuthorityType topAuthority = userInfo.getTopAuthority();
		
		List<AuthorityType> userScreen = new ArrayList<AuthorityType>();
		for(AuthorityType auth : AuthorityType.values()){
			if(!AuthorityType.GUEST.equals(auth) &&  topAuthority.getPriority() >=auth.getPriority()){
				userScreen.add(auth);
			}
		}
		String lang = request.getParameter("lang"); 
		
		if(lang !=null && !"".equals(lang)){
			Locale userLacle= LocaleConstants.parseLocaleString(lang);
			if( userLacle != null) {
				userInfo.setUserLocale(userLacle);
			}
		}
		
		request.getSession().setAttribute("var.user.screen", userScreen);
		
		return userInfo.getTopAuthority().mainPage();
	}
	
	

	/**
	 * Removes temporary authentication-related data which may have been stored
	 * in the session during the authentication process.
	 */
	protected final void clearAuthenticationAttributes(
			final HttpServletRequest request) {
		final HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

}