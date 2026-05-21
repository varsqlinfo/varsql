package com.varsql.web.util;

import java.util.Arrays;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 
 */
public final class SpringUtils {

	private SpringUtils() {}

	public static RequestMatcher[] toAntPathMatchers(String... patterns) {
	    return Arrays.stream(patterns)
	            .map(AntPathRequestMatcher::new)
	            .toArray(RequestMatcher[]::new);
	}

}

