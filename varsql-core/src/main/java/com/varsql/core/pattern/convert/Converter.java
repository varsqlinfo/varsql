package com.varsql.core.pattern.convert;

import java.util.List;

import com.varsql.core.pattern.parsing.TokenInfo;

/**
 * -----------------------------------------------------------------------------
* @fileName		: Converter.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface Converter {
	public String transform(String cont, TokenInfo... tokens);
	public String transform(String cont, TokenHandler handler, TokenInfo... tokens);
	
	public <T> List<T> tokenData(String cont, TokenInfo... tokens);
	public <T> List<T> tokenData(String cont, TokenHandler handler, TokenInfo... tokens);
	
}