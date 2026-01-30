package com.varsql.core.sql.mapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.db.DBVenderType;
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

	private final static String SQL_PARAM_START_TOKEN = "#{";
	private final static String SQL_PARAM2_START_TOKEN = "${";

	final static TokenInfo SQL_PARAM = new TokenInfo.Builder(SQL_PARAM_START_TOKEN, new String[] { "}" }).build();
	final static TokenInfo SQL_PARAM2 = new TokenInfo.Builder(SQL_PARAM2_START_TOKEN, new String[] { "}" }).build();
	
	final static String regex = "\\$\\{([^}]+)\\}";

    // 패턴 컴파일
    final static Pattern pattern = Pattern.compile(regex);

	public ConvertResult sqlParameter(DBVenderType dbType, String cont) {
		return sqlParameter(dbType, cont, null);
	}

	public ConvertResult sqlParameter(DBVenderType dbType, String cont, Map parameter) {
		return sqlParameter(dbType, cont, parameter, VarsqlConstants.SQL.PARAM.val());
	}
	public ConvertResult sqlParameter(DBVenderType dbType, String cont, Map parameter ,String replaceParamChar) {
		List<ParameterMapping> paramList = new LinkedList<ParameterMapping>();

		ConvertResult convertResult = super.tokenData(cont, new TokenHandler() {
			@Override
			public String beforeHandleToken(String str, TokenInfo converter) {
				
				if(SINGLEQUOTE.getStartDelimiter().equals(converter.getStartDelimiter()) || DOUBLEQUOTE.getStartDelimiter().equals(converter.getStartDelimiter())) {
					
					StringBuffer result = new StringBuffer();

					Matcher matcher = pattern.matcher(str);
					
					boolean findFlag = false; 
					while (matcher.find()) {
						findFlag = true; 
						String paramKey = matcher.group(1);
						ParameterMapping parameterMapping = new ParameterMapping.Builder(dbType, paramKey).build();
						matcher.appendReplacement(result, parameter ==null ? "" : (parameter.containsKey(parameterMapping.getProperty()) ? parameter.get(parameterMapping.getProperty()).toString() : ""));
			        }
					
					if(findFlag) {
						matcher.appendTail(result);
						return converter.getStartDelimiter()+result.toString() + converter.getEndDelimiter()[0];
					}else {
						return converter.getStartDelimiter()+str + converter.getEndDelimiter()[0];
					}
				}else if(SQL_PARAM_START_TOKEN.equals(converter.getStartDelimiter()) || SQL_PARAM2_START_TOKEN.equals(converter.getStartDelimiter())) {
					
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

		}, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK, SQL_PARAM, SQL_PARAM2);

		convertResult.setParameterInfo(paramList);

		return convertResult;
	}
}
