package com.varsql.core.pattern.parsing.function;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DelimiterFunction.java
* @DESC		: delimiter check function 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 3. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@FunctionalInterface
public interface StartDelimiterFunction {
	boolean test(String cont, int startIndex, char... chars);
}
