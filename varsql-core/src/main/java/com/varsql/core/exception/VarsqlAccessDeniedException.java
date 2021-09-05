package com.varsql.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @FileName  : VarsqlAccessDeniedException.java
 * @프로그램 설명 : 사용자 접근 제한 사용자.
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlAccessDeniedException extends AuthenticationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VarsqlAccessDeniedException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}


}
