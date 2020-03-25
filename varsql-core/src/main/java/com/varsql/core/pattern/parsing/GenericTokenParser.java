package com.varsql.core.pattern.parsing;

import java.util.Arrays;

import com.varsql.core.pattern.convert.ConverterException;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GenericTokenParser.java
* @DESC		: Generic Token Parser 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 3. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class GenericTokenParser implements TokenParser {

	@Override
	public String perform(TokenInfo tokenInfo, String val) {
		if (tokenInfo.getConvertFunction() == null) {
			return tokenInfo.isValueReturn() ? val : "";
		} else {
			return tokenInfo.getConvertFunction().apply(val);
		}
	}

	/**
	 * 
	 * @Method Name  : isStartDelimiter
	 * @Method 설명 : start delimiter check 
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 24. 
	 * @변경이력  :
	 * @param tokenInfo
	 * @param cont
	 * @param startIndex
	 * @param chars
	 * @return
	 */
	@Override
	public boolean isStartDelimiter(TokenInfo tokenInfo, String cont, int startIndex, char... chars) {
		if (tokenInfo.getStartDelimiterFunction() == null) {
			int charsLength = chars.length;
			int startDelimiterLen = tokenInfo.getStartDelimiterLen();
			if (startDelimiterLen == charsLength) {
				return Arrays.equals(chars, tokenInfo.getStartDelimiterChars());
			} else if (startDelimiterLen < charsLength) {
				return Arrays.equals(Arrays.copyOf(chars, startDelimiterLen), tokenInfo.getStartDelimiterChars());
			}
		} else {
			return tokenInfo.getStartDelimiterFunction().test(cont, startIndex, chars);
		}
		return false;
	}

	/**
	 * 
	 * @Method Name  : findEndDelimiterIndex
	 * @Method 설명 : find end delimiter 
	 * @작성자   : ytkim
	 * @작성일   : 2020. 3. 24. 
	 * @변경이력  :
	 * @param tokenInfo
	 * @param cont
	 * @param startIndex
	 * @return
	 */
	@Override
	public int findEndDelimiterIndex(TokenInfo tokenInfo, String cont, int startIndex) {
		if (tokenInfo.getEndDelimiterFunction() != null) {
			int idx = tokenInfo.getEndDelimiterFunction().findDelimiterIndex(cont, startIndex);

			if (idx > -1) {
				if (startIndex > idx) {
					throw new ConverterException(String.format("delimiter not valid start delimeter : %s ,lastidx %s",
							tokenInfo.getStartDelimiter(), idx));
				}
				return idx;
			}
		} else {
			if (tokenInfo.isMultipleEndDelimiter()) {
				for (String str : tokenInfo.getEndDelimiter()) {
					int idx = cont.indexOf(str, startIndex);

					if (idx > -1) {
						if (startIndex > idx) {
							throw new ConverterException(
									String.format("delimiter not valid start delimeter : %s , end delimiter %s", tokenInfo.getStartDelimiter(), str));
						}
						return idx + str.length() - 1;
					}
				}
			} else {
				int idx = cont.indexOf(tokenInfo.getEndDelimiter()[0], startIndex);

				if (idx > -1) {
					if (startIndex > idx) {
						throw new ConverterException(
								String.format("delimiter not valid start delimeter : %s , end delimiter %s", tokenInfo.getStartDelimiter(), tokenInfo.getEndDelimiter()[0]));
					}
					return idx + tokenInfo.getEndDelimiter()[0].length() - 1;
				}
			}
		}

		return -1;
	}

}
