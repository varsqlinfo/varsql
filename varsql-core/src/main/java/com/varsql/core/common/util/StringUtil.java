package com.varsql.core.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	public static String paramReplace(String delim, String str, String key, String replace){
		if(str.indexOf(delim) < 0) return str; 
		
		int position=0, firstIdx = 0 , lastIdx=-1; 
		
		int len = delim.length();
		
		String tmpParam = null, tmpValue=replace;
		StringBuilder sb = new StringBuilder(str);
		
		while( position <= sb.length() ){
			firstIdx = sb.indexOf(delim,position);
			
			if(firstIdx > -1){
				lastIdx = sb.indexOf(delim,firstIdx+len);
				
				if( lastIdx >-1){
					tmpParam =sb.substring(firstIdx+len, lastIdx);
					
					if(key.equals(tmpParam)){
						sb.replace(firstIdx, lastIdx+len , tmpValue);
						return sb.toString();
					}
					
					position = firstIdx+tmpValue.length();
				}else{
					break;
				}
			}else {
				break;
			}
		}
		
		return sb.toString();
	}
	
	public static String[] split(String a,  String delim){
		
		if("".equals(a)) return new String[0];
		
		int position=0;
		int delimiterIdx = 0; 
		int strLen = a.length();
		List<String> resultList = new ArrayList<String>();
		
		int len = delim.length();
		while(position <= strLen){
			delimiterIdx = a.indexOf(delim,position);
			if(delimiterIdx > -1){
				resultList.add(a.substring(position, delimiterIdx));
			}else {
				resultList.add(a.substring(position, strLen));
				break;
			}
			position = delimiterIdx+len;
		}
				
		return (String[]) resultList.toArray(new String[]{});
	}
	
	public static String allTrim(String s){
		if ( s == null ) return ""; 
			
		return s.replaceAll("\\p{Space}", "");
	}
	
	public boolean blankCheck(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ')
				return true;
		}
		return false;
	}
	
	public static boolean chkNullOrBlank(String str){
		if(str == null){
			return false;
		}
		
		if("".equals(str)){
			return false;
		}
		
		if("null".equals(str.toLowerCase())){
			return false;
		}
		
		return true;
	}
	
	public static String nullToString(String str){
		if(str == null){
			return "";
		}
		
		return str; 
	}
	
	public static String nullToString(String str, String initVal){
		if(str == null){
			return initVal;
		}
		
		return str; 
	}
	
	public static boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}
}
