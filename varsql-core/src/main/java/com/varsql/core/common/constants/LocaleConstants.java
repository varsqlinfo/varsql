package com.varsql.core.common.constants;

import java.util.Locale;


/**
 *
 * @FileName  : LocaleConstants.java
 * @프로그램 설명 : locale 변수 .
 * @Date      : 2019. 4. 17.
 * @작성자      : ytkim
 * @변경이력 :
 */
public enum LocaleConstants  {
	KO("ko") , EN ("en");

	private String locale;
	private String i18n;
	
	LocaleConstants(String locale){
		this.locale = locale;
		this.i18n = "lang."+locale ;
	}

	public String getLocale() {
		return locale;
	}

	public String getI18n() {
		return i18n;
	}

	public static Locale parseLocaleString(String locale) {
		return new Locale(getLocaleConstantsVal(locale).getLocale());
	}

	/**
	 * locale constants 정보 얻기
	 * @param locale
	 * @return
	 */
	public static LocaleConstants getLocaleConstantsVal(String locale) {
		if(locale==null) {
			locale = "";
		}else {
			locale = locale.toUpperCase();
		}
		
		String secondLocale = null;
		if(locale.length() > 2) {
			secondLocale = locale.substring(0,2);
		}

		for(LocaleConstants localeConstant : LocaleConstants.values()) {
			String localeCodeName = localeConstant.name(); 
			if(localeCodeName.equals(locale) || localeCodeName.equals(secondLocale)) {
				return localeConstant;
			}			
		}
		return LocaleConstants.KO;
	}
	
	

}

