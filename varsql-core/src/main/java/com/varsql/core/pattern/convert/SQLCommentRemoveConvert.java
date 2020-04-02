package com.varsql.core.pattern.convert;

import static com.varsql.core.sql.util.StringRegularUtils.regExpSpecialCharactersCheck;

import com.varsql.core.db.DBType;
import com.varsql.core.pattern.parsing.TokenInfo;
import com.varsql.core.pattern.parsing.function.EndDelimiterFunction;
import com.varsql.core.sql.util.StringRegularUtils;

public class SQLCommentRemoveConvert extends AbstractConverter {
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

	public String convert(String cont, DBType type) {
		return convert(cont, type, true);
	}

	public String convert(String cont, DBType type, boolean emptyLineRemove) {
		String result = "";
		switch (type) {
		case ORACLE:
			result = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, HINT_ORACLE2, LINE, HINT_ORACLE, BLOCK);
			break;

		case MYSQL:
			result = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE_MYSQL_IGNORE, LINE_MYSQL, BLOCK);
			break;

		case MARIADB:
			result = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, LINE_MYSQL_IGNORE, LINE_MYSQL, BLOCK);
			break;

		case SYBASE:
			result = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, LINE_SYBASE, BLOCK);
			break;
		default:
			result = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK);
			break;
		}
		if (emptyLineRemove) {
			return StringRegularUtils.removeBlank(result); // blank line remove
		}

		return result;
	}

}
