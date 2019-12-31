package com.varsql.core.sql.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.sql.SQLUtils;
import com.varsql.core.sql.format.VarsqlFormatterImpl;

/**
 * 
 * @FileName : VarsqlFormatterUtil.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql formatter을 하기 위한 것 
 * @변경이력	:
 */
public class SqlReplaceUtils {
	
	final public static String PARAM_DOLLAR = "$";
	final public static String PARAM_SHARP = "#"; 
	final public static String PARAM_REG ="([\\#|\\$]\\{(.+?)\\})|(/\\*)|(\\*/)|-{2}|([\r\n|\r|\n])";
	final public static String PARAM_REG_SINGLEQUOTE ="(['][\\#|\\$]\\{(.+?)\\}')|(/\\*)|(\\*/)|-{2}|([\r\n|\r|\n])";
	
	private SqlReplaceUtils(){};
	
	public static String paramReplace(String sql, boolean addSingleQuote){
		String regular =PARAM_REG;
		
		String dollar =PARAM_DOLLAR
				,sharp=PARAM_SHARP;
		
		if(!addSingleQuote){
			regular =PARAM_REG_SINGLEQUOTE;
			
			dollar = "'"+PARAM_DOLLAR;
			sharp = "'"+PARAM_SHARP;
		}
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(sql);
		
		boolean flag = matcher.find();
		if(flag) {
			StringBuffer buffer = new StringBuffer();
			boolean lineCommentStartFlag  = false ,mlineCommentStartFlag  = false;  
			while (flag) {
				String matchGroup  = matcher.group(0);
				
				if(matchGroup != null){
					if(matchGroup.startsWith(dollar)) {
						if(addSingleQuote){
							matcher.appendReplacement(buffer,"'"+matchGroup.replace("$","\\$")+"'");
						}else{
							matcher.appendReplacement(buffer, matchGroup.replace("$", "\\$").replaceAll("'", ""));
						}
					}else {
						if("/*".equals(matchGroup)) {
							mlineCommentStartFlag = true; 
						}
						if(mlineCommentStartFlag && "*/".equals(matchGroup)) {
							mlineCommentStartFlag = false; 
						}
						if(mlineCommentStartFlag) {
							flag = matcher.find();
							continue; 
						}
						
						if("--".equals(matchGroup)) {
							lineCommentStartFlag = true; 
						}
						if(lineCommentStartFlag && ("\r\n".equals(matchGroup) || "\n".equals(matchGroup)|| "\r".equals(matchGroup))) {
							lineCommentStartFlag = false; 
						}
						
						if(lineCommentStartFlag) {
							flag = matcher.find();
							continue; 
						}
						
						if(matchGroup.startsWith(sharp)) {
							if(addSingleQuote){
								matcher.appendReplacement(buffer, "'"+matchGroup+"'");
							}else{
								matcher.appendReplacement(buffer, matchGroup.replace("#", "\\#").replaceAll("'", ""));
							}
						}
					}
				}
				flag = matcher.find();
			}
			matcher.appendTail(buffer);
			
			return buffer.toString();
			
		}else {
			return sql;
		}
	}
}
