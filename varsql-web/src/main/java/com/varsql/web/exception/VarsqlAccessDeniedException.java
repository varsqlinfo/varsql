package com.varsql.web.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 사용자 접근 제한 사용자.
* 
* @fileName	: VarsqlAccessDeniedException.java
* @author	: ytkim
 */
public class VarsqlAccessDeniedException extends AuthenticationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VarsqlAccessDeniedException(String msg) {
		super(msg);
	}


}
