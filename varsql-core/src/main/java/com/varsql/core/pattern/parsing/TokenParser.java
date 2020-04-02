package com.varsql.core.pattern.parsing;
public interface TokenParser {

	public String perform(TokenInfo tokenInfo,String val);

	public boolean isStartDelimiter(TokenInfo tokenInfo, String cont, int startIndex, char... val);

	public TokenIndexInfo findEndDelimiterIndex(TokenInfo tokenInfo, String cont, int startIndex);
}
