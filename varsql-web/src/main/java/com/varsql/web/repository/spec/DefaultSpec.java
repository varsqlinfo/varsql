package com.varsql.web.repository.spec;

import java.text.MessageFormat;

public abstract class DefaultSpec {
	protected static String contains(String expression) {
	    return MessageFormat.format("%{0}%", expression);
	}
	
	protected static String startsWith(String expression) {
		return MessageFormat.format("{0}%", expression);
	}
	
	protected static String endsWith(String expression) {
		return MessageFormat.format("%{0}", expression);
	}
}
