package com.varsql.core.sql.mapping;

import java.util.LinkedList;
import java.util.List;

import com.varsql.core.pattern.convert.AbstractConverter;
import com.varsql.core.pattern.convert.TokenHandler;
import com.varsql.core.pattern.parsing.TokenInfo;

public class ParameterMappingUtil extends AbstractConverter {
	final static TokenInfo DOUBLEQUOTE = new TokenInfo.Builder("\"", new String[] { "\"" }).setValueReturn(false).build();

	final static TokenInfo SINGLEQUOTE = new TokenInfo.Builder("'", new String[] { "'" }).setValueReturn(false).build();

	// line commment
	final static TokenInfo LINE = new TokenInfo.Builder("--", new String[] { "\n", "\r\n" }).setValueReturn(false).build();

	final static TokenInfo BLOCK = new TokenInfo.Builder("/*", new String[] { "*/" }).setValueReturn(false).build();

	final static TokenInfo SQL_PARAM = new TokenInfo.Builder("#{", new String[] { "}" }).build();
	final static TokenInfo SQL_PARAM2 = new TokenInfo.Builder("${", new String[] { "}" }).build();
	
	public List<ParameterMapping> sqlParameter(String cont) {
		List<ParameterMapping> result = new LinkedList<ParameterMapping>();

		super.tokenData(cont, new TokenHandler() {
			@Override
			public String beforeHandleToken(String str, TokenInfo converter) {
				result.add(new ParameterMapping.Builder(str).build());
				return str;
			}
		}, DOUBLEQUOTE, LINE, BLOCK, SQL_PARAM, SQL_PARAM2);

		return result;
	}
}