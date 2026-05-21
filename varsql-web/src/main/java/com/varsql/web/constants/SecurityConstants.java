package com.varsql.web.constants;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SecurityConstants.java
* @DESC		: security constants
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2017. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class SecurityConstants {

    private SecurityConstants() {}
    public static final String REMEMBERME_KEY = "varsqlTokenKey";
    public static final String REMEMBERME_PARAMETER = "varsqlRememberMe";
    public static final String REMEMBERME_COOKIENAME = "VARSQLRTOKEN";
	
    public static final String JSESSION_ID_COOKIE_NAME = "VSQLJSID";
	
    public static final String SYSTEM_ID = "$SYSTEM$";
	
    public static final String LOGOUT_URL = "/logout";
	
    public static final String LOGIN_PROCESSING_URL = "/login_check";
	
    public static final String WEB_RESOURCES = "/webstatic/**";
}
