package com.varsql.core.sql.format;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.pattern.convert.SQLCommentRemoveConverter;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName : VarsqlFormatterUtil.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql formatter을 하기 위한 것
 * @변경이력	:
 */
public final class VarsqlFormatterUtil {

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

	public static String format(String sql, DBVenderType dbType){
		return format(sql , dbType , FORMAT_TYPE.DRUID);
	}

	public static String format(String sql, DBVenderType dbType, FORMAT_TYPE format_type){
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
	public static ResponseResult formatResponseResult(String sql, DBVenderType dbType){
		return formatResponseResult(sql, dbType, FORMAT_TYPE.DRUID);
	}

	public static ResponseResult formatResponseResult(String sql, DBVenderType dbType, FORMAT_TYPE format_type){
		ResponseResult result = new ResponseResult();

		String resultSql = "";

		sql = new SQLCommentRemoveConverter().convert(sql,dbType);

		if(format_type.equals(FORMAT_TYPE.VARSQL)){
			resultSql = new VarsqlFormatterImpl().execute(sql);
			//resultSql = SqlFormatter.format(sql);
		}else{

			try{

				resultSql = com.varsql.core.sql.util.SQLParserUtils.getParserString(sql,dbType);
			}catch(Exception e){
				resultSql =new VarsqlFormatterImpl().execute(sql);
				result.setMessage(e.getMessage());
			}
		}

		//resultSql = Native2Ascii.asciiToNative(resultSql);
		result.setItemOne(resultSql + BlankConstants.NEW_LINE);

		return result;
	}

	public static String ddlFormat(String sql, DBVenderType dbType){
		switch (dbType) {
			case TIBERO:
			case ORACLE:
				return sql;

			default:
				return sql;
		}

	}

	public static String addLastSemicolon(StringBuilder ddlStr, DDLCreateOption ddlOption) {
		String ddl = ddlStr.toString();

		ddl = StringUtils.rTrim(ddl);

		if(!ddl.endsWith(";")) {
			if(ddlOption.isAddLastSemicolon()) {
				return ddl+";";
			}
		}

		return ddl;
	}
}
