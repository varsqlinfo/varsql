package com.varsql.core.pattern.convert;

import com.varsql.core.pattern.parsing.TokenInfo;
import com.varsql.core.sql.util.StringRegularUtils;

public class CommentRemoveConverter extends AbstractConverter {
	final static TokenInfo DOUBLEQUOTE_IGNORE = new TokenInfo.Builder("\\\"", null, (val) -> "\\\"").build();
	final static TokenInfo DOUBLEQUOTE = new TokenInfo.Builder("\"", new String[] { "\"" },
			(val) -> "\"" + val + "\"").setEndDelimiterFunction((val, idx) -> {
				int reIdx = -1;
				int valLen = val.length();
				for (int i = idx; i < valLen; i++) {
					reIdx = val.indexOf('\"', i);
					int ignoreCharIdx = val.indexOf("\\\"", i);

					//System.out.println("function : "+idx+" :: "+reIdx +" :: "+ ignoreCharIdx);

					if (ignoreCharIdx == -1) {
						return reIdx;
					} else if (reIdx < ignoreCharIdx) {
						return reIdx;
					} else {
						if (ignoreCharIdx + 2 < reIdx) {
							return reIdx;
						}
					}
					if (reIdx > i) {
						i = reIdx;
					} else {
						break;
					}
				}
				return -1;
			}).build();

	final static TokenInfo SINGLEQUOTE_IGNORE = new TokenInfo.Builder("\\\'", null, (val) -> "\\\'").build();

	final static TokenInfo SINGLEQUOTE = new TokenInfo.Builder("'", new String[] { "'" },
			(val) -> "'" + val + "'").setEndDelimiterFunction((val, idx) -> {
				int reIdx = -1;
				int valLen = val.length();

				for (int i = idx; i < valLen; i++) {
					reIdx = val.indexOf('\'', i);
					int ignoreCharIdx = val.indexOf("\\\'", i);

					if (ignoreCharIdx == -1) {
						return reIdx;
					} else if (reIdx < ignoreCharIdx) {
						return reIdx;
					} else {
						if (ignoreCharIdx + 2 < reIdx) {
							return reIdx;
						}
					}
					if (reIdx > i) {
						i = reIdx;
					} else {
						break;
					}
				}
				return -1;
			}).build();

	final static TokenInfo REGULAR_EXPRESSION = new TokenInfo.Builder("/", new String[] { "/" },
			(val) -> "/" + val + "/").setStartDelimiterFunction((val, idx, chars) -> {
				
				for (int i = idx - 1; i >= 0; i--) {
					char c = val.charAt(i);
					if (!Character.isSpaceChar(c)) {
						if (Character.isDigit(c) || Character.isLetter(c)) {
							return false;
						} else {
							if (c == '(' || c == '=' || c == '!' || c == '&') {
								return true;
							}
						}
					}
				}

				return false;
			}).setEndDelimiterFunction((val, idx) -> {
				int reIdx = -1;
				int valLen = val.length();

					//System.out.println(val.charAt(idx)+": @@@@@ : " + val.substring(idx ,valLen > idx + 100 ?idx + 100 : valLen));

				for (int i = idx; i < valLen; i++) {
					reIdx = val.indexOf('/', i);
					int ignoreCharIdx = val.indexOf("\\/", i);

					if (ignoreCharIdx == -1) {
						return reIdx;
					} else if (reIdx < ignoreCharIdx) {
						return reIdx;
					} else if (ignoreCharIdx + 1 < reIdx) {
						return reIdx;
					}
					if (reIdx > i) {
						i = reIdx + 1;
					} else {
						break;
					}
				}
				return -1;
			}).build();

	// line commment
	final static TokenInfo LINE = new TokenInfo.Builder("//", new String[] { "\n", "\r\n" }, (val) -> "\n")
			.build();

	// property line comment
	final static TokenInfo LINE_IGNORE_PROPERTY = new TokenInfo.Builder("\\#", null, (val) -> "\\#").build();
	// property line comment
	final static TokenInfo LINE_PROPERTY = new TokenInfo.Builder("#", new String[] { "\n", "\r\n" },
			(val) -> "\n").build();

	final static TokenInfo BLOCK = new TokenInfo.Builder("/*", new String[] { "*/" }).setValueReturn(false)
			.build();

	final static TokenInfo BLOCK_JSP = new TokenInfo.Builder("<%--", new String[] { "--%>" })
			.setValueReturn(false).build();

	final static TokenInfo ELEMENT_IGNORE_START = new TokenInfo.Builder("<", new String[] { ">" }, (val) -> {
		return "<" + val + ">";
	}).build();
	final static TokenInfo ELEMENT_IGNORE_END = new TokenInfo.Builder("</", new String[] { ">" },
			(val) -> "</" + val + ">").build();
	final static TokenInfo BLOCK_HTML = new TokenInfo.Builder("<!--", new String[] { "-->" })
			.setValueReturn(false).build();

	static enum CommentType {
		JAVA, JSP, JAVASCRIPT, CSS, HTML, XML, PROPERTY;
	}

	public String convert(String cont, CommentType type) {
		return convert(cont, type, true);
	}

	public String convert(String cont, CommentType type, boolean emptyLineRemove) {
		String result = "";
		switch (type) {

		case JAVA:
			result = transform(cont, DOUBLEQUOTE_IGNORE, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK);
			break;
		case JSP:
			result = transform(cont, DOUBLEQUOTE_IGNORE, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK, BLOCK_JSP, BLOCK_HTML);
			break;
		case JAVASCRIPT:
			result = transform(cont, SINGLEQUOTE_IGNORE, DOUBLEQUOTE_IGNORE, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK,
					REGULAR_EXPRESSION);
			break;
		case CSS:
			result = transform(cont, DOUBLEQUOTE_IGNORE, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK);
			break;
		case HTML:
			result = convert(cont, CommentType.XML, false);
			result = convert(result, CommentType.JAVASCRIPT, false);
			break;
		case XML:
			result = transform(cont, DOUBLEQUOTE_IGNORE, BLOCK_HTML, ELEMENT_IGNORE_START, ELEMENT_IGNORE_END);
			break;
		case PROPERTY:
			result = transform(cont, LINE_IGNORE_PROPERTY, LINE_PROPERTY);
			break;
		default:
			break;
		}
		if (emptyLineRemove) {
			return StringRegularUtils.removeBlank(result); // blank line remove
		}

		return result;
	}
}
