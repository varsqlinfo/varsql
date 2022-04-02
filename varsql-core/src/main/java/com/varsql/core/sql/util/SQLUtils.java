package com.varsql.core.sql.util;

import com.varsql.core.db.DBVenderType;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName : SQLUtils.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql query utils
 * @변경이력	:
 */
public final class SQLUtils {

	private SQLUtils(){};

	public static String escapeValue(Object val) {
		if(val==null) return null;

		String s = val.toString();

		StringBuffer sb = new StringBuffer();
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '\'':
				sb.append("''");
				break;
			case '\\':
				sb.append("\\");
				break;
			case '\b':
				sb.append("\b");
				break;
			case '\f':
				sb.append("\f");
				break;
			case '\n':
				sb.append("\n");
				break;
			case '\r':
				sb.append("\r");
				break;
			case '\t':
				sb.append("\t");
				break;
			case '/':
				sb.append("/");
				break;
			default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	public static String generateSelectQuery(String tblName, String conditionQuery, DBVenderType dbType) {

		StringBuilder reqQuerySb = new StringBuilder().append("select * from ").append(tblName).append(" where 1=1 ");

		if (!StringUtils.isBlank(conditionQuery)) {
			conditionQuery = StringUtils.lTrim(conditionQuery).toLowerCase();
			if (conditionQuery.startsWith("where")) {
				conditionQuery = conditionQuery.replaceFirst("where", "");
			}

			if (conditionQuery.startsWith("and")) {
				conditionQuery = conditionQuery.replaceFirst("and", "");
			}

			conditionQuery = " and " + conditionQuery;
			reqQuerySb.append(conditionQuery);
		}

		return reqQuerySb.toString();
	}
}
