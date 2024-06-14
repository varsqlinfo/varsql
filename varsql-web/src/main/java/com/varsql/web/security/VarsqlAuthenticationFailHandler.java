package com.varsql.web.security;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.constants.HttpHeaderConstants;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.exception.BlockingUserException;
import com.vartech.common.utils.StringUtils;

@Component
public class VarsqlAuthenticationFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
            throws IOException, ServletException {
    	
    	String locale = req.getParameter(HttpParamConstants.LANG_LOCALE);
    	String paramLocale = "";
    	if(!StringUtils.isBlank(locale)) {
			paramLocale="&locale="+ locale;
		}
    	
    	if(ex instanceof BlockingUserException){
    		req.getRequestDispatcher("/error/blockingUser").forward(req, res);
    	}else{
    		req.getRequestDispatcher("/login?mode=fail"+ paramLocale).forward(req, res);
    	}
    }
 
}