package com.varsql.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @FileName  : BlockingUserException.java
 * @프로그램 설명 : 사용자 block
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class BlockingUserException extends AuthenticationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BlockingUserException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}


}
