package com.varsql.core.common.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	
	private static List<String> ALL_LANG;
	
	static {
		ALL_LANG = new ArrayList<String>();

		for(LocaleConstants item : LocaleConstants.values()){
			ALL_LANG.add(item.name().toLowerCase());
		}
	}
	
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
		if(locale != null) {
			
			LocaleConstants reval= null; 
			String upperCaseStr = locale.toUpperCase(); 
			if(ALL_LANG.contains(upperCaseStr)) {
				reval = LocaleConstants.valueOf(upperCaseStr);
			}else {
				if(locale.length() > 2) {
					locale = locale.substring(0,2);
					
					upperCaseStr = locale.toUpperCase();
					
					if(ALL_LANG.contains(upperCaseStr)) {
						reval = LocaleConstants.valueOf(upperCaseStr);
					}
				}
			}
			
			if(reval !=null) {
				return new Locale(reval.getLocale());
			}
		}
		return null;
	}

}

