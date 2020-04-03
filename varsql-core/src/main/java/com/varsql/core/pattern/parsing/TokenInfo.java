package com.varsql.core.pattern.parsing;

import com.varsql.core.pattern.parsing.function.ConvertFunction;
import com.varsql.core.pattern.parsing.function.EndDelimiterFunction;
import com.varsql.core.pattern.parsing.function.StartDelimiterFunction;

/**
 * -----------------------------------------------------------------------------
* @fileName		: TokenInfo.java
* @desc		: 토큰 정보.  
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class TokenInfo {
	private String startDelimiter;
	private char[] startDelimiterChars;
	private int startDelimiterLen;

	private String[] endDelimiter;
	private char[] endDelimiterChars;
	private int endDelimiterLen;
	private ConvertFunction convertFunction;

	private boolean valueReturn;
	private boolean isEmptyEndDelimiter;

	private StartDelimiterFunction startDelimiterFunction;
	private EndDelimiterFunction endDelimiterFunction;
	private boolean multipleEndDelimiter;

	@SuppressWarnings("unused")
	private TokenInfo() {
	}

	public String getStartDelimiter() {
		return startDelimiter;
	}

	public char[] getStartDelimiterChars() {
		return startDelimiterChars;
	}

	public int getStartDelimiterLen() {
		return startDelimiterLen;
	}

	public String[] getEndDelimiter() {
		return endDelimiter;
	}

	public char[] getEndDelimiterChars() {
		return endDelimiterChars;
	}

	public int getEndDelimiterLen() {
		return endDelimiterLen;
	}

	public ConvertFunction getConvertFunction() {
		return convertFunction;
	}

	public boolean isValueReturn() {
		return valueReturn;
	}

	public boolean isEmptyEndDelimiter() {
		return isEmptyEndDelimiter;
	}

	public StartDelimiterFunction getStartDelimiterFunction() {
		return startDelimiterFunction;
	}

	public EndDelimiterFunction getEndDelimiterFunction() {
		return endDelimiterFunction;
	}

	public boolean isMultipleEndDelimiter() {
		return multipleEndDelimiter;
	}

	public static class Builder {
		private TokenInfo compositeConverter;

		public Builder(String startDelimiter) {
			this(startDelimiter, null);
		}

		public Builder(String startDelimiter, String[] endDelimiter) {
			this(startDelimiter, endDelimiter, null);
		}

		public Builder(String startDelimiter, String[] endDelimiter, ConvertFunction convertFunction) {
			this.compositeConverter = new TokenInfo();
			this.compositeConverter.startDelimiter = startDelimiter;
			this.compositeConverter.startDelimiterChars = startDelimiter.toCharArray();
			this.compositeConverter.startDelimiterLen = this.compositeConverter.startDelimiterChars.length;

			if (endDelimiter != null && endDelimiter.length > 0) {
				this.compositeConverter.endDelimiter = endDelimiter;
				this.compositeConverter.endDelimiterChars = endDelimiter[0].toCharArray();
				this.compositeConverter.multipleEndDelimiter = this.compositeConverter.endDelimiter.length > 1 ? true
						: false;

			} else {
				this.compositeConverter.endDelimiter = null;
				this.compositeConverter.multipleEndDelimiter = false;
				this.compositeConverter.endDelimiterChars = new char[0];
			}
			this.compositeConverter.endDelimiterLen = this.compositeConverter.endDelimiterChars.length;
			this.compositeConverter.convertFunction = convertFunction;
			this.compositeConverter.valueReturn = true;
		}

		public Builder setConvertFunction(ConvertFunction convertFunction) {
			this.compositeConverter.convertFunction = convertFunction;
			return this;
		}

		public Builder setStartDelimiterFunction(StartDelimiterFunction startDelimiterFunction) {
			this.compositeConverter.startDelimiterFunction = startDelimiterFunction;
			return this;
		}

		public Builder setEndDelimiterFunction(EndDelimiterFunction endDelimiterFunction) {
			this.compositeConverter.endDelimiterFunction = endDelimiterFunction;
			return this;
		}

		public Builder setValueReturn(boolean valueReturn) {
			this.compositeConverter.valueReturn = valueReturn;
			return this;
		}

		private void resolve() {
			this.compositeConverter.isEmptyEndDelimiter = this.compositeConverter.endDelimiter == null
					&& this.compositeConverter.endDelimiterFunction == null;
		}

		public TokenInfo build() {
			resolve();
			return this.compositeConverter;
		}
	}
}
