package com.varsql.web.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

import com.varsql.core.common.util.RequestUtil;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.VartechUtils;

@Component
public class VarsqlAccessDeniedHandler implements AccessDeniedHandler {
	private String accessDeniedUrl;
	private String csrfDeniedUrl;

	private static final Logger logger = LoggerFactory.getLogger(VarsqlAccessDeniedHandler.class);

	public VarsqlAccessDeniedHandler() {
	}

	public VarsqlAccessDeniedHandler(String accessDeniedUrl) {
		this(accessDeniedUrl , "");
	}
	public VarsqlAccessDeniedHandler(String accessDeniedUrl  , String csrfDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
		this.csrfDeniedUrl = accessDeniedUrl;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		if(RequestUtil.isAjaxRequest(request)){

			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.OK.value());

			ResponseResult result = new ResponseResult();

			if(SecurityUtil.isAuthenticated()){
				if (accessDeniedException instanceof CsrfException && !response.isCommitted()) {
					result.setResultCode(ResultConst.CODE.PRECONDITION_FAILED.toInt());
				}else{
					result.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
				}
			}else{
				result.setResultCode(ResultConst.CODE.LOGIN_INVALID.toInt());
			}

			Writer writer=null;
			try {
				writer = response.getWriter();
				writer.write(VartechUtils.objectToJsonString(result));
			} catch (IOException e) {
				logger.error("VarsqlAccessDeniedHandler " ,e);
			}finally{
				if(writer!=null){ try {writer.close();} catch (IOException e) {}};
			}
		}else{
			if (accessDeniedException instanceof CsrfException && !response.isCommitted()) {
	            // Remove the session cookie so that client knows it's time to obtain a new CSRF token
	            String pCookieName = "CSRF-TOKEN";
	            Cookie cookie = new Cookie(pCookieName, "");
	            cookie.setMaxAge(0);
	            cookie.setHttpOnly(false);
	            cookie.setPath("/");
	            response.addCookie(cookie);
	            response.sendRedirect(request.getContextPath()+csrfDeniedUrl);
	        }else{
	        	response.sendRedirect(request.getContextPath()+accessDeniedUrl);
	        }
		}
	}

	public String getAccessDeniedUrl() {
		return accessDeniedUrl;
	}

	public void setAccessDeniedUrl(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}
}