package com.varsql.web.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlBasicAuthenticationEntryPoint.java
* @desc		: BasicAuthenticationEntryPoint configuration
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Component
public class VarsqlBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	private final Logger logger = LoggerFactory.getLogger(VarsqlBasicAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException {

		logger.warn("VarsqlBasicAuthenticationEntryPoint url : {}, parameter : {} ",request.getRequestURL(), HttpUtils.getServletRequestParam(request));
		logger.warn("cookie values : {} " , HttpUtils.getAllCookieString(request));
		logger.warn("request header : {} " , HttpUtils.getAllReqHeaderString(request));
		logger.warn("response header : {} " , HttpUtils.getAllResHeaderString(response));
		logger.warn("varsqlBasicAuthenticationEntryPoint commence : {}", authEx.getMessage(), authEx);
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
		//response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		if(VarsqlUtils.isAjaxRequest(request)){

			ResponseResult result = new ResponseResult();
			response.setContentType(VarsqlConstants.JSON_CONTENT_TYPE);
			response.setStatus(HttpStatus.OK.value());
			result.setResultCode(RequestResultCode.PRECONDITION_FAILED);

			try (Writer writer= response.getWriter()){
				writer.write(VartechUtils.objectToJsonString(result));
			} catch (IOException e) {
				logger.error("exceptionRequestHandle Cause :" + e.getMessage() ,e);
			}
		}else{

			try {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/error/invalidToken");
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e1) {
				logger.error("commence Cause :" + e1.getMessage() ,e1);
			}
		}

		//response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("varsql");
		super.afterPropertiesSet();
	}
}