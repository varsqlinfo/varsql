package com.varsql.core.auth;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.varsql.core.exception.BlockingUserException;
 
public class VarsqlAuthenticationFailHandler implements AuthenticationFailureHandler {
    
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
            throws IOException, ServletException {
    	
    	if(ex instanceof BlockingUserException){
    		req.getRequestDispatcher("/error/blockUser").forward(req, res);
    	}else{
    		req.getRequestDispatcher("/login?mode=fail").forward(req, res);
    	}
    }
 
}