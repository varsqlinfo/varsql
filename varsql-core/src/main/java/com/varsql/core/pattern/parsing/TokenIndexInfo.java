package com.varsql.core.pattern.parsing;

/**
 * -----------------------------------------------------------------------------
* @fileName		: TokenIndexInfo.java
* @desc		: end token find index info 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class TokenIndexInfo {
	private int idx;
	private int delimiterLength;

	public TokenIndexInfo(int idx, int delimiterLength) {
		this.idx = idx;
		this.delimiterLength = delimiterLength;
	}

	public int getIdx() {
		return idx;
	}

	public int getDelimiterLength() {
		return delimiterLength;
	}
}