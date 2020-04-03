package com.varsql.core.pattern.convert;

import com.varsql.core.pattern.parsing.TokenInfo;

/**
 * -----------------------------------------------------------------------------
* @fileName		: TokenHandler.java
* @desc		: 토큰 내부 컨텐츠 핸들러 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface TokenHandler {
	public String beforeHandleToken(String str, TokenInfo tokenInfo);

	default String afterHandleToken(String str, TokenInfo tokenInfo) {
		return str;
	};
}
