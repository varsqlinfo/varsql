package com.varsql.core.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @FileName  : SimpleSsoHandler.java
* @프로그램 설명 : sso hanlder
* @Date      : 2019. 11. 26.
* @작성자      : ytkim
* @변경이력 :
*/
public class SimpleSsoHandler extends AbstractSsoHandler{
	private final static Logger logger = LoggerFactory.getLogger(SimpleSsoHandler.class);

	final String SSO_TOKEN = "ssoToken";

	@Override
	public boolean beforeSsoHandler(HttpServletRequest request, HttpServletResponse response) {

		String ssoToken = request.getParameter(SSO_TOKEN);

		logger.debug("ssoToken : {} ", ssoToken);

		if(StringUtils.isBlank(ssoToken)) {
			return false;
		}

		return true;
	}

	@Override
	public String handler(HttpServletRequest request, HttpServletResponse response) {
		boolean autoLogin = "Y".equalsIgnoreCase(request.getParameter("59d8b888657b3fa591b092204e2cc3b4"));

		String ssoToken = request.getParameter(SSO_TOKEN);

		String usrId = "";
		if(autoLogin) {
			usrId = ssoToken;
		}else {
			// TODO sso 처리 할것.
		}

		if(null != ssoToken && !"".equals(ssoToken)) {
			logger.debug("sso handler ssoToken : {} ", ssoToken);
		}

		return usrId;
	}
}
