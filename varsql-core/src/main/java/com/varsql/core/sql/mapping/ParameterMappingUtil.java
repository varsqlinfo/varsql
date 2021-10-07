package com.varsql.core.sql.mapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.db.DBType;
import com.varsql.core.pattern.convert.AbstractConverter;
import com.varsql.core.pattern.convert.ConvertResult;
import com.varsql.core.pattern.convert.TokenHandler;
import com.varsql.core.pattern.parsing.TokenInfo;

public class ParameterMappingUtil extends AbstractConverter {
	final static TokenInfo DOUBLEQUOTE = new TokenInfo.Builder("\"", new String[] { "\"" }).setValueReturn(true).build();

	final static TokenInfo SINGLEQUOTE = new TokenInfo.Builder("'", new String[] { "'" }).setValueReturn(true).build();

	// line commment
	final static TokenInfo LINE = new TokenInfo.Builder("--", new String[] { "\n", "\r\n" }).setValueReturn(true).build();

	final static TokenInfo BLOCK = new TokenInfo.Builder("/*", new String[] { "*/" }).setValueReturn(true).build();

	final private static String SQL_PARAM_START_TOKEN = "#{";
	final private static String SQL_PARAM2_START_TOKEN = "${";

	final static TokenInfo SQL_PARAM = new TokenInfo.Builder(SQL_PARAM_START_TOKEN, new String[] { "}" }).build();
	final static TokenInfo SQL_PARAM2 = new TokenInfo.Builder(SQL_PARAM2_START_TOKEN, new String[] { "}" }).build();

	public ConvertResult sqlParameter(DBType dbType, String cont) {
		return sqlParameter(dbType, cont, null);
	}

	public ConvertResult sqlParameter(DBType dbType, String cont, Map parameter) {
		return sqlParameter(dbType, cont, parameter, VarsqlConstants.SQL.PARAM.val());
	}
	public ConvertResult sqlParameter(DBType dbType, String cont, Map parameter ,String replaceParamChar) {
		List<ParameterMapping> paramList = new LinkedList<ParameterMapping>();

		ConvertResult convertResult = super.tokenData(cont, new TokenHandler() {
			@Override
			public String beforeHandleToken(String str, TokenInfo converter) {

				if(SQL_PARAM_START_TOKEN.equals(converter.getStartDelimiter()) || SQL_PARAM2_START_TOKEN.equals(converter.getStartDelimiter())) {

					ParameterMapping parameterMapping = new ParameterMapping.Builder(dbType, str).build();

					if(SQL_PARAM_START_TOKEN.equals(converter.getStartDelimiter())){
						paramList.add(parameterMapping);
						return replaceParamChar==null ? str : replaceParamChar;
					}else {
						return parameter ==null ? null : (parameter.containsKey(parameterMapping.getProperty()) ? parameter.get(parameterMapping.getProperty()).toString() : null);
					}
				}else {
					return converter.getStartDelimiter()+str + converter.getEndDelimiter()[0];
				}
			}

		}, DOUBLEQUOTE, LINE, BLOCK, SQL_PARAM, SQL_PARAM2);

		convertResult.setParameterInfo(paramList);

		return convertResult;
	}
}
