package com.varsql.core.pattern.convert;

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
	public ConvertResult transform(String cont, TokenInfo... tokens);
	public ConvertResult transform(String cont, TokenHandler handler, TokenInfo... tokens);
	
	public ConvertResult tokenData(String cont, TokenInfo... tokens);
	public ConvertResult tokenData(String cont, TokenHandler handler, TokenInfo... tokens);
	
}