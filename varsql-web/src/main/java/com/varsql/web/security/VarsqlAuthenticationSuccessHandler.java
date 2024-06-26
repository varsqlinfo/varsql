package com.varsql.web.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.AuthorityTypeImpl;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.common.service.UserCommonService;
import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.security.rememberme.RememberMeHttpServletRequestWapper;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.CommUtils;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : VarsqlAuthenticationSuccessHandler.java
 * @프로그램 설명 : 로그인 성공시
 * @Date      : 2019. 6. 14.
 * @작성자      : ytkim
 * @변경이력 :
 */
@Component
public class VarsqlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(VarsqlAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private RequestCache requestCache = new HttpSessionRequestCache();

	final private SecurityLogService securityLogService;
	
	final private UserCommonService userCommonService;
	
	public VarsqlAuthenticationSuccessHandler(SecurityLogService securityLogService, UserCommonService userCommonService) {
		super();
		super.setUseReferer(true);
		this.securityLogService = securityLogService;
		this.userCommonService = userCommonService;
	}
	
	public void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {

		User userInfo = SecurityUtil.loginInfo();
		String targetUrl = userRedirectTargetUrl(request, response, userInfo, authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to {} ", targetUrl);
			return;
		}
		
		String lang = request.getParameter("lang");
		Locale userLocale = null; 
		if(!StringUtils.isBlank(lang)){
			userLocale= LocaleConstants.parseLocaleString(lang);
			if( userLocale != null && !userLocale.getLanguage().equals(userInfo.getUserLocale().getLanguage())) {
				userCommonService.changeUserLocale(lang, userInfo);
				userInfo.setUserLocale(userLocale);
			}
		}else {
			userLocale = userInfo.getUserLocale();
		}
		
		VarsqlUtils.changeLocale(request, response, userLocale);
		
		securityLogService.addLog(userInfo, userInfo.isLoginRememberMe()?"auto" :"login", CommUtils.getClientPcInfo(request));

		if(userInfo.isLoginRememberMe()) {
			try {
				userCommonService.reloadDatabaseList(); // database information reload
	    	}catch(Throwable e) {
	    		logger.error("database information reload error {} ", e.getMessage());
	    	}
			super.clearAuthenticationAttributes(request);

			String queryStr = request.getQueryString();
			//String reqUrl = request.getRequestURI().replaceFirst(request.getContextPath(), "") +(StringUtil.isBlank(queryStr)?"":"?"+queryStr);
			String reqUrl = request.getRequestURI().replaceFirst(request.getContextPath(), "");
			
			logger.debug("remember me forward request uri : {}, query string :{}" , reqUrl , queryStr);
			logger.debug("cookie values : {} " , HttpUtils.getAllCookieString(request));
			logger.debug("request header : {} " , HttpUtils.getAllReqHeaderString(request));
			logger.debug("response header : {} " , HttpUtils.getAllResHeaderString(response));
			logger.debug("----------------------------------------------------------------------");
			request.getRequestDispatcher(reqUrl).forward(new RememberMeHttpServletRequestWapper(request, response), response);
			
			/*
			if(VarsqlUtils.isAjaxRequest(request)) {
				logger.debug("remember me forward request uri : {}, query string :{}" , reqUrl , queryStr);
				request.getRequestDispatcher(reqUrl).forward(request, response);
			}else {
				logger.debug("remember me sendRedirect request uri : {}, query string :{}" , reqUrl , queryStr);
				redirectStrategy.sendRedirect(request, response, reqUrl);
			}
			*/
			
		    return ;
		}else {

			if(!VarsqlUtils.isAjaxRequest(request)) {
				SavedRequest savedRequest = requestCache.getRequest(request, response);

			    if(savedRequest != null) {

			    	String contextPath =request.getContextPath();

			    	int contextPosIdx = targetUrl.indexOf(contextPath);

			    	if(contextPosIdx > -1) {
			    		String url = targetUrl.substring(contextPosIdx + contextPath.length());
				    	if(!"".equals(url) && !"/".equals(url)) {
				    		targetUrl = savedRequest.getRedirectUrl();
				    	}
			    	}
			    }
			}
		    logger.debug("login targer url : {}", targetUrl);
			redirectStrategy.sendRedirect(request, response, targetUrl);
			super.clearAuthenticationAttributes(request);
		}
	}

	private String userRedirectTargetUrl(HttpServletRequest request, HttpServletResponse response, User userInfo, final Authentication authentication) {

		AuthorityType topAuthority = userInfo.getTopAuthority();

		List<AuthorityType> userScreen = new ArrayList<AuthorityType>();
		for(AuthorityType auth : AuthorityTypeImpl.values()){
			if(!AuthorityTypeImpl.GUEST.equals(auth) &&  topAuthority.getPriority() >=auth.getPriority()){
				userScreen.add(auth);
			}
		}

		request.getSession().setAttribute(HttpSessionConstants.USER_SCREEN, userScreen);

		return userInfo.getTopAuthority().mainPage();
	}
}