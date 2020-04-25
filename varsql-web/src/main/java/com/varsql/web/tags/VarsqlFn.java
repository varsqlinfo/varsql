package com.varsql.web.tags;

import com.varsql.web.constants.WebStaticResourceVersion;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.DateUtils;

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
		return VarsqlUtils.objectToString(json);
	}

	public static String currentDate(String foramt) {
		return DateUtils.getCurrentDate(foramt);
	}

	public static boolean isRuntimelocal(String foramt) {
		return VarsqlUtils.isRuntimelocal();
	}

	public static long randomVal(int val) {
		return java.lang.Math.round(java.lang.Math.random() * val);
	}

	public static String pubJsVersion() {
		if(VarsqlUtils.isRuntimelocal()) {
			return randomVal(10000)+"";
		}else {
			return WebStaticResourceVersion.PUB_JS;
		}
	}

	public static String staticResourceVersion(String type) {
		if("codemirror".equals(type)) {
			return WebStaticResourceVersion.CODE_MIRROR;
		}else if("css".equals(type)) {
			return WebStaticResourceVersion.VARSQL_CSS;
		}else if("prettify".equals(type)) {
			return WebStaticResourceVersion.PRETTIFY;
		}else {
			return randomVal(10000)+"";
		}
	}


}
