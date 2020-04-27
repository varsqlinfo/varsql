package com.varsql.core.sql.format;

import java.util.List;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.varsql.core.sql.util.SqlReplaceUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.Native2Ascii;

/**
 * 
 * @FileName : VarsqlFormatterUtil.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql formatter을 하기 위한 것 
 * @변경이력	:
 */
public final class VarsqlFormatterUtil {
	final private static SQLParserFeature[] DEFAULT_FEATURES = { SQLParserFeature.KeepComments, SQLParserFeature.SkipComments };
	final private static FormatOption formatOpt = new FormatOption(VisitorFeature.OutputPrettyFormat,VisitorFeature.OutputSkipSelectListCacheString);
	static{
		formatOpt.setUppCase(false);
	}
	
	public enum FORMAT_TYPE {
		VARSQL("VQRSQL"), DRUID("DRUID");
		
		private String typeName; 
		
		FORMAT_TYPE(String name){
			this.typeName = name; 
		}
		
		public String getTypeName(){
			return this.typeName;
		}
	}
	
	private VarsqlFormatterUtil(){};
	
	public static String format(String sql){
		return SQLUtils.format(sql, null);
	}
	
	public static String format(String sql, String dbType){
		return format(sql , dbType , FORMAT_TYPE.DRUID);
	}
	
	public static String format(String sql, String dbType, FORMAT_TYPE format_type){
		return formatResponseResult(sql, dbType, format_type).getItem();
	}
	
	/**
	 * 
	 * @Method Name  : formatResponseResult
	 * @Method 설명 : format 처리.
	 * @작성일   : 2018. 2. 20. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param sql
	 * @param dbType
	 * @param format_type
	 * @return
	 */
	public static ResponseResult formatResponseResult(String sql, String dbType){
		return formatResponseResult(sql, dbType, FORMAT_TYPE.DRUID);
	}
	
	public static ResponseResult formatResponseResult(String sql, String dbType, FORMAT_TYPE format_type){
		ResponseResult result = new ResponseResult();
		
		String resultSql = "";
				
		if(format_type.equals(FORMAT_TYPE.VARSQL)){
			resultSql = new VarsqlFormatterImpl().execute(sql); 
		}else{
			try{
				sql = SqlReplaceUtils.paramReplace(sql, true);
				SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, DEFAULT_FEATURES);
				
				parser.setKeepComments(true);
				List<SQLStatement> statementList = parser.parseStatementList();
				
				resultSql =SQLUtils.toSQLString(statementList, dbType, null, formatOpt);
				
				resultSql = SqlReplaceUtils.paramReplace(resultSql, false);
			}catch(Exception e){
				resultSql =new VarsqlFormatterImpl().execute(sql);
				result.setMessage(e.getMessage());
			}
		}
		
		//resultSql = Native2Ascii.asciiToNative(resultSql); 
		result.setItemOne(resultSql);
		
		return result;
		
	}
}
