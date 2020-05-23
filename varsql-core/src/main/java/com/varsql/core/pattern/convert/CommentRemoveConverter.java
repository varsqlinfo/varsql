package com.varsql.core.pattern.convert;

import static com.varsql.core.pattern.StringRegularUtils.regExpSpecialCharactersCheck;

import com.varsql.core.pattern.StringRegularUtils;
import com.varsql.core.pattern.parsing.TokenInfo;
import com.varsql.core.pattern.parsing.function.EndDelimiterFunction;

/**
 * -----------------------------------------------------------------------------
* @fileName		: CommentRemoveConverter.java
* @desc		: 프로그램(java, jsp, javascript, css, html, xml, property) 코멘트 지우기 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class CommentRemoveConverter extends AbstractConverter {
	// comment type
	public static enum CommentType {
		JAVA, JSP, JAVASCRIPT, CSS, HTML, XML, PROPERTY;
	}

	final static TokenInfo SINGLEQUOTE = new TokenInfo.Builder("'", new String[] { "'" }, (val) -> "'" + val + "'")
	.setEndDelimiterFunction((val, idx) -> {
		return regExpSpecialCharactersCheck('\'', val, idx);
	}).build();

	final static TokenInfo REGULAR_EXPRESSION = new TokenInfo.Builder("/", new String[] { "/" },
		(val) -> "/" + val + "/").setStartDelimiterFunction((val, idx, chars) -> {

			String reservedWord = StringRegularUtils.trim(val.substring(idx - 20 > 0 ? idx - 20 : 0, idx));
			// 정규식 앞에 올수 있는 예약어
			if (reservedWord.endsWith("return") || reservedWord.endsWith("typeof")
					|| reservedWord.endsWith("delete") || reservedWord.endsWith("throw")) {
				return true;
			}

			for (int i = idx - 1; i >= 0; i--) {
				char c = val.charAt(i);
				if (!Character.isSpaceChar(c)) { // 공백은 제외 하고 체크. 
					return ":(=!&[|,?~><^+".indexOf(c) > -1 ? true : false;
				}
			}
			return false;
		}).setEndDelimiterFunction((val, startIdx) -> {
			int reIdx = -1;
			int valLen = val.length();

			int newLineIdx = Math.min(val.indexOf('\n', startIdx), val.indexOf("\r", startIdx));

			//System.out.println(val.charAt(startIdx)+": @@@@@ : " + val.substring(startIdx ,valLen > startIdx + 100 ?startIdx + 100 : valLen));
			for (int i = startIdx; i < valLen; i++) {
				reIdx = regExpSpecialCharactersCheck('/', val, i);

				if (reIdx > -1) {
					if (reIdx > newLineIdx) {
						return -1;
					}
					// regular expression "[/]" check
					if (isIncludeBracket(val, reIdx, i)) {
						i = reIdx;
					} else {
						return reIdx;
					}
				} else {
					return -1;
				}
			}

			return reIdx;
	}).build();

	//new line -> mac = "\r" , window = "\r\n" , linux = "\n"
	private static String[] NEW_LINE_ARR = new String[] { "\n", "\r" };
	
	// newline check
	private static EndDelimiterFunction newLineEndDelFn = (val, startIdx) -> {
		int newLineIdx = val.indexOf('\n', startIdx);
		int newLineIdx2 = val.indexOf("\r", startIdx);

		return Math.min(newLineIdx, newLineIdx2);
	};

	// line comment
	final static TokenInfo LINE = new TokenInfo.Builder("//", NEW_LINE_ARR, (val) -> LINE_SEPARATOR)
			.setEndDelimiterFunction(newLineEndDelFn).build();

	// property line comment
	final static TokenInfo LINE_IGNORE_PROPERTY = new TokenInfo.Builder("\\#", null, (val) -> "\\#").build();
	// property line comment
	final static TokenInfo LINE_PROPERTY = new TokenInfo.Builder("#", NEW_LINE_ARR, (val) -> LINE_SEPARATOR)
			.setEndDelimiterFunction(newLineEndDelFn).build();

	final static TokenInfo BLOCK = new TokenInfo.Builder("/*", new String[] { "*/" }).setValueReturn(false).build();

	final static TokenInfo BLOCK_JSP = new TokenInfo.Builder("<%--", new String[] { "--%>" }).setValueReturn(false)
			.build();

	final static TokenInfo ELEMENT_IGNORE_START = new TokenInfo.Builder("<", new String[] { ">" }, (val) -> {
		return "<" + val + ">";
	}).build();
	final static TokenInfo ELEMENT_IGNORE_END = new TokenInfo.Builder("</", new String[] { ">" }, (val) -> "</" + val + ">").build();
	final static TokenInfo BLOCK_HTML = new TokenInfo.Builder("<!--", new String[] { "-->" }).setValueReturn(false).build();

	final static TokenInfo DOUBLEQUOTE = new TokenInfo.Builder("\"", new String[] { "\"" }, (val) -> "\"" + val + "\"")
	.setEndDelimiterFunction((val, idx) -> {
		return regExpSpecialCharactersCheck('"', val, idx);
	}).build();

	public String convert(String cont, CommentType type) {
		return convert(cont, type, true);
	}
	
	public String convert(String cont, CommentType type, boolean emptyLineRemove) {
		ConvertResult convertResult=null;
		
		String result=null;
		
		switch (type) {
		case JAVA:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK);
			break;
		case JSP:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK, BLOCK_JSP, BLOCK_HTML);
			break;
		case JAVASCRIPT:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK, REGULAR_EXPRESSION);
			break;
		case CSS:
			convertResult = transform(cont, DOUBLEQUOTE, SINGLEQUOTE, LINE, BLOCK);
			break;
		case HTML:
			result = convert(cont, CommentType.XML, emptyLineRemove);
			result = convert(result, CommentType.JAVASCRIPT, emptyLineRemove);
			break;
		case XML:
			convertResult = transform(cont, BLOCK_HTML, ELEMENT_IGNORE_START, ELEMENT_IGNORE_END);
			break;
		case PROPERTY:
			convertResult = transform(cont, LINE_IGNORE_PROPERTY, LINE_PROPERTY);
			break;
		default:
			result = convert(cont, CommentType.JAVA, emptyLineRemove);
			break;
		}
		
		if(convertResult != null) {
			result = convertResult.getCont();
		}
		
		if (emptyLineRemove) {
			return StringRegularUtils.removeBlank(result); // blank line remove
		}

		return result;
	}
	
	private static boolean isIncludeBracket(String val, int includeCharIdx, int startIdx) {
		int reIdx = -1;
		int valLen = val.length();

		for (int i = startIdx; i < valLen; i++) {
			reIdx = regExpSpecialCharactersCheck('[', val, i);

			if (reIdx > -1) {
				int rightBracketIdx = regExpSpecialCharactersCheck(']', val, reIdx);

				if (reIdx < includeCharIdx) {
					if (rightBracketIdx == -1 || includeCharIdx < rightBracketIdx) {
						return true;
					}
				} else {
					return false;
				}

				if (rightBracketIdx > -1) {
					i = rightBracketIdx;
				}
			} else {
				return false;
			}
		}

		return false;
	}
}
