package com.varsql.core.sql.format;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @FileName : SqlFormatterInterface.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql formatter을 하기 위한 것 
 * @변경이력	:
 */
public interface VarsqlFormatterInterface {
	public Map<String, HashMap<String, String>> getSqlClause();
	
	public String getRegularExpression();
	
	public Map<String, String> getFormatCluse();
	
}
