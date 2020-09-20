package com.varsql.core.common.util;

import org.stringtemplate.v4.ST;

/**
 *
 * @FileName  : VarsqlJdbcUtil.java
 * @프로그램 설명 :
 * @Date      : 2019. 6. 9.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlJdbcUtil {
	private VarsqlJdbcUtil() {}

	public static String getJdbcUrl(String urlFormat,String serverip,String port,String databaseName) {
		return getJdbcUrl(urlFormat, serverip, port, databaseName ,"");
	}
	public static String getJdbcUrl(String urlFormat,String serverip,String port,String databaseName, String opt) {

		//String urlFormat = "jdbc:oracle:thin:@{serverip}{if(port)}:{port}{endif}:{databaseName}";

		ST bbb = new ST(urlFormat, '{', '}');

		bbb.add("serverip", serverip);
		bbb.add("port", isPort(port)?port:false);
		bbb.add("databaseName", databaseName);

		return bbb.render();
	}

	private static boolean isPort(String val) {
		int numVal=-1;
		try {
			numVal = Integer.parseInt(val);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }

	    return numVal < 1 ?false: true;
	}
}
