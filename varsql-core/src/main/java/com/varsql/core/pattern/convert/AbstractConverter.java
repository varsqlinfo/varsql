package com.varsql.core.pattern.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.poi.ss.formula.functions.T;

import com.varsql.core.pattern.parsing.GenericTokenParser;
import com.varsql.core.pattern.parsing.TokenIndexInfo;
import com.varsql.core.pattern.parsing.TokenInfo;
import com.varsql.core.pattern.parsing.TokenParser;


public abstract class AbstractConverter implements Converter {
	TokenParser parser;
	
	final static String LINE_SEPARATOR = System.lineSeparator();

	public AbstractConverter() {
		this.parser = new GenericTokenParser();
	}
	
	public AbstractConverter(TokenParser parser) {
		this.parser = parser;
	}

	@Override
	public String transform(String cont, TokenInfo... tokens) {
		return transform(cont, null, tokens);
	}

	@Override
	public String transform(String cont, TokenHandler handler, TokenInfo... tokens) {
		cont = cont + System.lineSeparator();
		final StringBuilder dest = new StringBuilder();
		final Stack<TokenInfo> states = new Stack<>();

		int startIdx = -1;
		int contLen = cont.length();
		char c1, c2, c3, c4;

		for (int index = 0; index < contLen; index++) {

			if (states.empty()) {
				c1 = cont.charAt(index);
				c2 = (index + 1) < contLen ? cont.charAt(index + 1) : '0';
				c3 = (index + 2) < contLen ? cont.charAt(index + 2) : '0';
				c4 = (index + 3) < contLen ? cont.charAt(index + 3) : '0';

				// start char
				TokenInfo startPattern = null;

				// pattern check
				for (TokenInfo pattern : tokens) {
					if (parser.isStartDelimiter(pattern, cont, index, c1, c2, c3, c4)) {
						startPattern = pattern;
						break;
					}
				}

				if (startPattern != null) {
					index = index + startPattern.getStartDelimiterLen();

					if (startPattern.isEmptyEndDelimiter()) {
						dest.append(getHandlerValue(handler, startPattern, ""));
					} else {
						states.push(startPattern);
						startIdx = index;
					}
				} else {
					dest.append(c1);
				}

			} else {
				TokenInfo statePattern = states.peek();
				TokenIndexInfo tokenDelimiterFindInfo = parser.findEndDelimiterIndex(statePattern, cont, startIdx);

				int suffixIdx = tokenDelimiterFindInfo == null ? -1 : tokenDelimiterFindInfo.getIdx();

				if (suffixIdx > -1) {
					index = suffixIdx + tokenDelimiterFindInfo.getDelimiterLength() - (tokenDelimiterFindInfo.getDelimiterLength() > 0 ? 1 : 0);

					try {
						dest.append(getHandlerValue(handler, statePattern, cont.substring(startIdx, suffixIdx)));
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
					states.pop();

				} else {
					dest.append(cont.substring(startIdx - statePattern.getStartDelimiterLen(), startIdx));
					states.pop();
				}

			}
		}
		return dest.toString();
	}

	private String getHandlerValue(TokenHandler handler, TokenInfo startPattern, String token) {
		
		if (handler == null)
			return parser.perform(startPattern, token);

		String val = handler.beforeHandleToken(token, startPattern);
		val = parser.perform(startPattern, val);
		return handler.afterHandleToken(val, startPattern);
	}

	@Override
	public List<T> tokenData(String cont, TokenInfo... tokens) {
		return tokenData(cont, null, tokens);
	}

	@Override
	public List<T> tokenData(String cont, TokenHandler handler, TokenInfo... tokens) {
		cont = cont + System.lineSeparator();

		final Stack<TokenInfo> states = new Stack<>();

		int startIdx = -1;
		int contLen = cont.length();
		char c1, c2, c3, c4;
		List result = new ArrayList();
		for (int index = 0; index < contLen; index++) {

			if (states.empty()) {
				c1 = cont.charAt(index);
				c2 = (index + 1) < contLen ? cont.charAt(index + 1) : '0';
				c3 = (index + 2) < contLen ? cont.charAt(index + 2) : '0';
				c4 = (index + 3) < contLen ? cont.charAt(index + 3) : '0';

				// start char
				TokenInfo startPattern = null;
				// pattern check
				for (TokenInfo pattern : tokens) {
					if (parser.isStartDelimiter(pattern, cont, index, c1, c2, c3, c4)) {
						startPattern = pattern;
						break;
					}
				}

				if (startPattern != null) {

					index = index + startPattern.getStartDelimiterLen();

					if (startPattern.isEmptyEndDelimiter()) {
						if (startPattern.isValueReturn()) {
							result.add(getHandlerValue(handler, startPattern, ""));
						}
					} else {
						states.push(startPattern);
						startIdx = index;
					}
				}
			} else {
				TokenInfo statePattern = states.peek();

				TokenIndexInfo tokenDelimiterFindInfo = parser.findEndDelimiterIndex(statePattern, cont, startIdx);

				int suffixIdx = tokenDelimiterFindInfo == null ? -1 : tokenDelimiterFindInfo.getIdx();

				if (suffixIdx > -1) {
					index = suffixIdx + tokenDelimiterFindInfo.getDelimiterLength()	- (tokenDelimiterFindInfo.getDelimiterLength() > 0 ? 1 : 0);

					if (statePattern.isValueReturn()) {
						result.add(getHandlerValue(handler, statePattern, cont.substring(startIdx, suffixIdx)));
					}
					states.pop();
				} else {
					states.pop();
				}

			}
		}
		return result;
	}
}
