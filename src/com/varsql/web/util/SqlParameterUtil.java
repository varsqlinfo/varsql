package com.varsql.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.varsql.db.vo.SqlStatement;

/**
 *날짜 관련 util
 * @author ytkim 
*/
public class SqlParameterUtil {
	public static SqlStatement substituteVariables(String template, Map<String, String> variables) {
	    Pattern pattern = Pattern.compile("[\\#|\\$]\\{(.+?)\\}");
	    Matcher matcher = pattern.matcher(template);
	    
	    boolean flag = matcher.find();
	    if(flag) {
		    // StringBuilder cannot be used here because Matcher expects StringBuffer
		    StringBuffer buffer = new StringBuffer();
		    List paramList = new ArrayList();
		    while (flag) {
		    	String matchGroup  = matcher.group(0);
		    	String variableKey  = matcher.group(1);
		    	
		    	if(matchGroup.startsWith("$")) {
		    		if (variables.containsKey(variableKey)) {
		    			matcher.appendReplacement(buffer, variables.get(variableKey));
		 	        }else {
		 	        	matcher.appendReplacement(buffer, "");
		 	        }
		    	}else {
		    		if (variables.containsKey(variableKey)) {
		 	            // quote to work properly with $ and {,} signs
		 	            paramList.add(variables.get(variableKey));
		 	        }else {
		 	        	paramList.add("");
		 	        }
		    		matcher.appendReplacement(buffer, "?");
		    	}
		    	flag = matcher.find();
		    }
		    matcher.appendTail(buffer);
		    
		    String sql = buffer.toString();
		    
		    return new SqlStatement( sql, paramList);
		    
	    }else {
	    	return new SqlStatement( template, null);
	    }
	}
}
