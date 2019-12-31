package com.varsql.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 
 * @FileName  : RestAuthenticationEntryPoint.java
 * @프로그램 설명  : 로그인  redirect 처리
 * @Date      : 2015. 6. 22. 
 * @작성자      : ytkim
 * @변경이력 :
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