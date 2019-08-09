package com.varsql.app.common.beans;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
 
public class CamelCaseMap extends HashMap {
    
	private static final long serialVersionUID = -7700790403928325865L;
	
	@Override
	public Object put(Object key, Object value) {
		return super.put(toCamelCase(key.toString()), value);
	}
	
	public static String toCamelCase(String target) {
		StringBuffer buffer = new StringBuffer();
		for (String token : target.toLowerCase().split("_")){
			buffer.append(StringUtils.capitalize(token));
		}
		
		return StringUtils.uncapitalize(buffer.toString());
	}
   
}