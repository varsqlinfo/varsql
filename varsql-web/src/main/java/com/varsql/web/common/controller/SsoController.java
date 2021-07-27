package com.varsql.web.common.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.common.code.VarsqlSsoType;
import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.core.sso.SsoHandler;
import com.varsql.web.common.sso.SsoBeanFactory;
import com.varsql.web.common.sso.SsoComponent;
import com.varsql.web.constants.ResourceConfigConstants;

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
			return new ModelAndView("redirect:/");
		}
		logger.debug("SsoController req uri : {} " , request.getRequestURI());

    	String usrid = ssoHandler.handler(request, response);

    	Authentication auth = ssoComponent.ssoLogin(usrid);

    	if(auth == null ) {
    		return new ModelAndView("redirect:/");
    	}

		return new ModelAndView("redirect:" + AuthorityType.USER.getMainPage());
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


