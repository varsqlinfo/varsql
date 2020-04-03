package com.varsql.core.pattern.parsing;

/**
 * -----------------------------------------------------------------------------
* @fileName		: TokenParser.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface TokenParser {

	public String perform(TokenInfo tokenInfo,String val);

	public boolean isStartDelimiter(TokenInfo tokenInfo, String cont, int startIndex, char... val);

	public TokenIndexInfo findEndDelimiterIndex(TokenInfo tokenInfo, String cont, int startIndex);
}
