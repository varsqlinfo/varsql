package com.varsql.core.pattern.parsing.function;

/**
 * -----------------------------------------------------------------------------
* @fileName		: EndDelimiterFunction.java
* @desc		: end token find index function 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@FunctionalInterface
public interface EndDelimiterFunction {
	public int findDelimiterIndex(String cont, int startIndex);
}
