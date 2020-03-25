package com.varsql.core.pattern.parsing.function;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: EndDelimiterFunction.java
* @DESC		: delimiter check function 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 3. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@FunctionalInterface
public interface EndDelimiterFunction {
	public int findDelimiterIndex(String cont, int startIndex);
}
