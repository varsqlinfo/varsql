package com.varsql.web.security;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.varsql.core.exception.BlockingUserException;

@Component
public class VarsqlAuthenticationFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
            throws IOException, ServletException {
    	
    	if(ex instanceof BlockingUserException){
    		req.getRequestDispatcher("/error/blockingUser").forward(req, res);
    	}else{
    		req.getRequestDispatcher("/login?mode=fail").forward(req, res);
    	}
    }
 
}