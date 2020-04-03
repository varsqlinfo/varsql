package com.varsql.core.pattern.parsing.function;

/**
 * -----------------------------------------------------------------------------
* @fileName		: StartDelimiterFunction.java
* @desc		: start token check 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@FunctionalInterface
public interface StartDelimiterFunction {
	boolean test(String cont, int startIndex, char... chars);
}
