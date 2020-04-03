package com.varsql.core.pattern.parsing.function;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ConvertFunction.java
* @desc		:  
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@FunctionalInterface
public interface ConvertFunction {
	String apply(String str);
}
