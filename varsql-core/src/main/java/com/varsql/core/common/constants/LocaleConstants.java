package com.varsql.core.common.constants;

import java.util.Locale;

import com.vartech.common.utils.StringUtils;


/**
 *
 * @FileName  : LocaleConstants.java
 * @프로그램 설명 : locale 변수 .
 * @Date      : 2019. 4. 17.
 * @작성자      : ytkim
 * @변경이력 :
 */
public enum LocaleConstants {
	KO("ko") , EN ("en");

	private String locale;
	private String i18n;

	LocaleConstants(String locale){
		this.locale = locale;
		this.i18n = locale +".lang";
	}

	public String getLocale() {
		return locale;
	}

	public String getI18n() {
		return i18n;
	}

	public static Locale parseLocaleString(String locale) {
		if(!StringUtils.isBlank(locale)) {
			LocaleConstants reval = getLocaleConstantsVal(locale.toUpperCase());

			if(reval !=null) {
				return new Locale(reval.getLocale());
			}
		}
		return null;
	}

	/**
	 * locale constants 정보 얻기
	 * @param locale
	 * @return
	 */
	private static LocaleConstants getLocaleConstantsVal(String locale) {
		String secondLocale = null;
		if(locale.length() > 2) {
			secondLocale = locale.substring(0,2);
		}

		LocaleConstants secondLocaleConstants= null;
		for(LocaleConstants localeConstant : LocaleConstants.values()) {
			if(localeConstant.name().equals(locale)) {
				return localeConstant;
			}

			if(localeConstant.name().equals(secondLocale)) {
				secondLocaleConstants = localeConstant;
			}
		}
		return secondLocaleConstants;
	}

}

