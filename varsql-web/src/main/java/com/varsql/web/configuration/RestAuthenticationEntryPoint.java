package com.varsql.web.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * -----------------------------------------------------------------------------
* @fileName		: RestAuthenticationEntryPoint.java
* @desc		: rest auth 처리
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2015. 6. 22. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		String xRequestedWith = request.getHeader("X-Requested-With");

		if (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith)) {
			request.getRequestDispatcher("/invalidLogin").forward(request, response);
		}else{
			response.sendRedirect(request.getContextPath()+"/login");
		}
	}
}