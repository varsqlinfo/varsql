package com.varsql.core.pattern.parsing.function;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ConvertFunction.java
* @DESC		: convert function 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 3. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@FunctionalInterface
public interface ConvertFunction {
	String apply(String str);
}
