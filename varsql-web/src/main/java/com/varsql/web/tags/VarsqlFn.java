package com.varsql.web.tags;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.web.constants.WebStaticResourceVersion;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlFn.java
* @desc		: varsql custom tag function
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class VarsqlFn{

	public static String objectToJson(Object json) {
		return VartechUtils.objectToJsonString(json);
	}

	public static String currentDate(String format) {
		return VarsqlDateUtils.format(format);
	}

	public static boolean isRuntimelocal() {
		return VarsqlUtils.isRuntimeLocal();
	}

	public static long randomVal(Integer val) {
		return java.lang.Math.round(java.lang.Math.random() * val);
	}

	public static String pubJsVersion() {
		if(VarsqlUtils.isRuntimeLocal()) {
			return randomVal(10000)+"";
		}else {
			return WebStaticResourceVersion.PUB_JS;
		}
	}

	public static String staticResourceVersion(String type) {
		if("codeEditor_ver".equals(type)) {
			return WebStaticResourceVersion.CODE_EDITOR;
		}else if("css".equals(type)) {
			return WebStaticResourceVersion.VARSQL_CSS;
		}else if("prettify".equals(type)) {
			return WebStaticResourceVersion.PRETTIFY;
		}else {
			if(VarsqlUtils.isRuntimeLocal()) {
				return randomVal(10000)+"";
			}else {
				return WebStaticResourceVersion.STATIC_RESOURCE;
			}
		}
	}

	public static String contextPath(HttpServletRequest request) {
		return request.getContextPath();
	}

	public static String loginUrl(HttpServletRequest request) {
		return request.getContextPath()+"/login_check";
	}

	public static String logoutUrl(HttpServletRequest request) {
		return request.getContextPath()+"/logout";
	}
	
	public static boolean isPasswordResetModeManager() {
		return Configuration.getInstance().getPasswordResetMode().equals(VarsqlConstants.PASSWORD_RESET_MODE.MANAGER);
	}
	
	public static boolean isPasswordResetModeEmail() {
		return Configuration.getInstance().getPasswordResetMode().equals(VarsqlConstants.PASSWORD_RESET_MODE.EMAIL);
	}
	
	public static boolean isManager() {
		return SecurityUtil.isManager();
	}
	
	public static boolean isAdmin() {
		return SecurityUtil.isAdmin();
	}
	
	/**
	 * app support locale 
	 * 
	 * @param locale locale
	 * @return "ko_kr" -> ko, en_us -> en
	 */
	public static String requestLocaleCode(HttpServletRequest request) {
		String locale = request.getParameter("locale");
		if(locale != null) {
			return LocaleConstants.getLocaleConstantsVal(locale).getLocaleCode();
		}
		
		Locale appLocale = VarsqlUtils.getAppLocale(request);
		
		if(appLocale != null) {
			return LocaleConstants.localeToLocaleCode(appLocale); 
		}
		
		return LocaleConstants.localeToLocaleCode(request.getLocale());
		
	}
}
