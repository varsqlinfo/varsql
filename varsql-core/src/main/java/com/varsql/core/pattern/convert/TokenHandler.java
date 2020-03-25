package com.varsql.core.pattern.convert;

import com.varsql.core.pattern.parsing.TokenInfo;

public interface TokenHandler {
	public String beforeHandleToken(String str, TokenInfo tokenInfo);

	default String afterHandleToken(String str, TokenInfo tokenInfo) {
		return str;
	};
}
