package com.varsql.web.repository;

import java.text.MessageFormat;

public interface DefaultJpaRepository {
	default String contains(String expression) {
	    return MessageFormat.format("%{0}%", expression);
	}
	
	default String startsWith(String expression) {
		return MessageFormat.format("{0}%", expression);
	}
	
	default String endsWith(String expression) {
		return MessageFormat.format("%{0}", expression);
	}
}
