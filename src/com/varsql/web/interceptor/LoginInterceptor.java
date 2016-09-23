package com.varsql.web.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//import com.circlook.common.constants.CircConstants;
//import com.circlook.common.constants.bean.CF_CIRCLER;

public class LoginInterceptor extends HandlerInterceptorAdapter 
{
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 
	{
//		//컨트롤러 실행전에 호출
//		String usrId =getSessionValue(request.getSession(),CF_CIRCLER.CIRC_ID);
//		
//		if("".equals(usrId)){
//			
//			// ajax 접근
//			if( "XMLHttpRequest".equals(request.getHeader("x-requested-with")) ) {
//				doExpired(response);
//			}else{  // 일반 접근.
//				response.sendRedirect( request.getContextPath()+"/logout");
//			}
//			return false;
//		}
		
		String url = request.getRequestURI();
		
		logger.info("before mobile controller...[" + url + "]");
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		//컨트롤러 실행후에 호출

		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
		//모든 작업이 완료된후 실행
		
		HttpSession session = request.getSession();
		
		
	}
	
	private String getSessionValue(HttpSession session, String key){
		return (session.getAttribute("usrId") == null)?"":session.getAttribute("usrId").toString();
	}
	
	/**
	 * 
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void doExpired(HttpServletResponse response) throws UnsupportedEncodingException, IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<script charset=\"utf-8\">");
		sb.append("	$(function(){");
		sb.append("		var agent = navigator.userAgent;");
		sb.append("		if (agent.match(/iPhone/) != null || agent.match(/iPod/) != null) {");
		sb.append("			window.location = \"jscall://gotoMainPage\";");
		sb.append("		} else {");
		sb.append("			window.locatin.href='/';");
		sb.append("		}");
		sb.append("	});");
		sb.append("</script>");
		sb.append("</head>");
		sb.append("</html>");
		
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentLength(sb.toString().getBytes("utf-8").length);
		
		response.getWriter().print(sb.toString());
	}
}
