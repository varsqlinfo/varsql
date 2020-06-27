package com.varsql.core.pattern.convert;

import static com.varsql.core.pattern.StringRegularUtils.regExpSpecialCharactersCheck;

import com.varsql.core.db.DBType;
import com.varsql.core.pattern.StringRegularUtils;
import com.varsql.core.pattern.parsing.TokenInfo;
import com.varsql.core.pattern.parsing.function.EndDelimiterFunction;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SQLCommentRemoveConverter.java
* @desc		: sql 코렌트 지우기
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SQLCommentRemoveConverter extends AbstractConverter {

	final static TokenInfo SINGLEQUOTE = new TokenInfo.Builder("'", new String[] { "'" }, (val) -> "'" + val + "'")
			.setEndDelimiterFunction((val, idx) -> {
				return regExpSpecialCharactersCheck('\'', val, idx);
			}).build();

	//new line -> mac = "\r" , window = "\r\n" , linux = "\n"
	private static String[] NEW_LINE_ARR = new String[] { "\n", "\r" };

	private static EndDelimiterFunction newLineEndDelFn = (val, startIdx) -> {
		int newLineIdx = val.indexOf('\n', startIdx);
		int newLineIdx2 = val.indexOf("\r", startIdx);

		return Math.min(newLineIdx, newLineIdx2);
	};

	// line comment
	final static TokenInfo LINE = new TokenInfo.Builder("--", NEW_LINE_ARR).setValueReturn(false).setEndDelimiterFunction(newLineEndDelFn).build();
	final static TokenInfo LINE_SYBASE = new TokenInfo.Builder("//", NEW_LINE_ARR).setValueReturn(false).setEndDelimiterFunction(newLineEndDelFn).build();

	final static TokenInfo MYBATIS_PARAM_IGNORE = new TokenInfo.Builder("#{", null, (val) -> "#{")  .build();

	final static TokenInfo MYBATIS_PARAM_IGNORE2 = new TokenInfo.Builder("${", null, (val) -> "${")  .build();

	// mysql line comment
	final static TokenInfo LINE_MYSQL_IGNORE = new TokenInfo.Builder("\\#", null, (val) -> "\\#").build();


	final static TokenInfo LINE_MYSQL = new TokenInfo.Builder("#", NEW_LINE_ARR).setValueReturn(false).setEndDelimiterFunction(newLineEndDelFn).build();

	final static TokenInfo HINT_ORACLE = new TokenInfo.Builder("/*+", new String[] { "*/" }, (val) -> {
		return "/*+" + val + "*/";
	}).setValueReturn(true).build();

	final static TokenInfo HINT_ORACLE2 = new TokenInfo.Builder("--+", null, (val) -> "--+").setValueReturn(true).build();

	final static TokenInfo BLOCK = new TokenInfo.Builder("/*", new String[] { "*/" }).setValueReturn(false).build();

	final static TokenInfo DOUBLEQUOTE = new TokenInfo.Builder("\"", new String[] { "\"" }, (val) -> "\"" + val + "\"")	.setEndDelimiterFunction((val, idx) -> {
		return regExpSpecialCharactersCheck('"', val, idx);
	}).build();

	public String convert(String cont, String type) {
		return convert(cont, DBType.getDBType(type));
	}

	public String convert(String cont, String type, boolean emptyLineRemove) {
		return convert(cont, DBType.getDBType(type), emptyLineRemove);
	}

	public String convert(String cont, DBType type) {
		return convert(cont, type, true);
	}

	public String convert(String cont, DBType type, boolean emptyLineRemove) {
		ConvertResult convertResult=null;


		switch (type) {
		case ORACLE:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, HINT_ORACLE2, LINE, HINT_ORACLE, BLOCK);
			break;

		case MYSQL:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, MYBATIS_PARAM_IGNORE, MYBATIS_PARAM_IGNORE2, LINE_MYSQL_IGNORE, LINE_MYSQL, BLOCK);
			break;

		case MARIADB:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, MYBATIS_PARAM_IGNORE, MYBATIS_PARAM_IGNORE2, LINE_MYSQL_IGNORE, LINE_MYSQL, BLOCK);
			break;

		case SYBASE:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, LINE_SYBASE, BLOCK);
			break;
		default:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK);
			break;
		}

		String result=convertResult.getCont();

		if (emptyLineRemove) {
			return StringRegularUtils.removeBlank(result); // blank line remove
		}

		return result;
	}

}
