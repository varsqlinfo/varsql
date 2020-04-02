package com.varsql.core.pattern.parsing;

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