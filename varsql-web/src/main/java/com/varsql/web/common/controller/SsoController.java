package com.varsql.web.common.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.auth.AuthorityTypeImpl;
import com.varsql.core.common.code.VarsqlSsoType;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.core.sso.SsoHandler;
import com.varsql.web.common.sso.SsoBeanFactory;
import com.varsql.web.common.sso.SsoComponent;
import com.varsql.web.constants.ResourceConfigConstants;
import com.vartech.common.utils.StringUtils;

/**
*
*
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SsoLoginController.java
* @DESC		: sso 컨트롤러
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
 DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 2. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
*/
@Controller
@Conditional(SsoController.SsoUrlCondition.class)
@RequestMapping("/sso")
public class SsoController implements InitializingBean  {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(SsoController.class);

	@Autowired
	@Qualifier(ResourceConfigConstants.APP_SSO_BEAN_FACTORY)
    private SsoBeanFactory ssoBeanFactory;

	@Autowired
	@Qualifier(ResourceConfigConstants.APP_SSO_COMPONENT)
	private SsoComponent ssoComponent;

	private SsoHandler ssoHandler;

	@RequestMapping(value="/proc" )
	public ModelAndView proc(HttpServletRequest request,HttpServletResponse response, ModelAndView mav)  {
		/*
		if(!VarsqlSsoType.URL.equals(VarsqlWebConfig.getInstance().getSsoConfig().getMode())) {
			return new ModelAndView("redirect:/");
		}
		*/

		if(!ssoHandler.beforeSsoHandler(request, response)) {
			String loginUrl = ssoHandler.loginURL(request, response); 
			if(StringUtils.isBlank(loginUrl)) {
				return new ModelAndView("redirect:/");
			}
			return new ModelAndView("redirect:"+ loginUrl);
		}
		
		String usrid = ssoHandler.handler(request, response);
		
		logger.debug("req ssoId : {}, uri : {} " , usrid, request.getRequestURI());

    	Authentication auth = ssoComponent.ssoLogin(usrid);

    	if(auth == null ||!SecurityUtil.isAdmin(auth)) {
    		
    		logger.debug("auth : {} " , auth);
    		
    		request.setAttribute("ssoId", usrid);
    		
    		ssoHandler.afterSsoHandler(request, response, auth);
    		
    		if(!SecurityUtil.isAnonymous()) {
				new SecurityContextLogoutHandler().logout(request, null, null);
			}
    		
    		return getLoginRedirectMav(request, ssoHandler.loginURL(request, response));
    	}
    	
    	String redirectUrl = ssoHandler.successRedirectURL(request, response);
    	
    	if(StringUtils.isBlank(redirectUrl)) {
    		return new ModelAndView("redirect:" + AuthorityTypeImpl.USER.getMainPage());
    	}
    	
    	return new ModelAndView("redirect:" + redirectUrl);
	}

	// login redirect ModelAndView
	private ModelAndView getLoginRedirectMav(HttpServletRequest request, String redirectLoginPath) {
		Map<String, Object> model = new HashMap<String, Object>();

		Enumeration<String> names = request.getAttributeNames();
		if (names.hasMoreElements()) {
			String name = names.nextElement();
			model.put(name, request.getAttribute(name));
		}

		return new ModelAndView(redirectLoginPath, model);
	}


    @Override
    public void afterPropertiesSet() throws ServletException {
    	this.ssoHandler = ssoBeanFactory.getSsoBean();
    }

    public static class SsoUrlCondition implements Condition {
    	@Override
    	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    		return VarsqlSsoType.URL.equals(VarsqlWebConfig.getInstance().getSsoConfig().getMode());
    	}
    }

}


