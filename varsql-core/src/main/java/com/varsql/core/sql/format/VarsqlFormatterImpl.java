package com.varsql.core.sql.format;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
/**
 * 
 * @FileName : SqlFormatterDb2.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: db2 sql fomatter 
 * @변경이력	:
 */
public class VarsqlFormatterImpl extends VarsqlFormatter{
	
	public final static String COMPFILE1 ="com/ytkim/app/tdi/text/QueryText";

	public VarsqlFormatterImpl() {
		super();
	}
	public VarsqlFormatterImpl(String sql) {
		super(sql);
	}
	
	public Map<String, HashMap<String, String>> getSqlClause() {
		
		Map<String,HashMap<String ,String>> SQL_CLAUSE = super.getSqlClause();
		
		SQL_CLAUSE.put("with", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
			put(OPT_BEFOREINDENT,"-1");
		}});
		
		SQL_CLAUSE.put("when", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
			put(OPT_BEFOREINDENT,"-1");
			put(OPT_LASTINDENT,"1");
		}});
		
		SQL_CLAUSE.put("then", new HashMap<String,String>(){{
			put(OPT_BEFORE,white_string);
			put(OPT_LAST,new_line_string);
			put(OPT_BEFOREINDENT,"-1");

		}});
		
		return SQL_CLAUSE;
	}
}
