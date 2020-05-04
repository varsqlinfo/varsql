package com.varsql.web.util;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.varsql.core.common.constants.VarsqlConstants;

public final class QueryDslUtils {
	
	public static StringTemplate toChar(Expression<?> expr) {
		return toChar(expr , VarsqlConstants.DATE_FORMAT);
	}
	
	public static StringTemplate toChar(Expression<?> expr, String datePattern ) {
		return Expressions.stringTemplate("to_char({0},'{1s}')", expr, ConstantImpl.create(datePattern)); 
	}
}
