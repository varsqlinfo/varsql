package com.varsql.web.util;

import java.util.Date;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateExpression;
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
	
	public static DateExpression<Date> toDate(Expression<String> expr) {
		return toDate(expr , VarsqlConstants.DATE_FORMAT);
	}
	public static DateExpression<Date> toDate(Expression<String> expr, String pattern) {
        return Expressions.dateTemplate(Date.class, "to_date({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }
}
