package com.varsql.web.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * user blocking exception 
* 
* @fileName	: BlockingUserException.java
* @author	: ytkim
 */
public class BlockingUserException extends AuthenticationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BlockingUserException(String msg) {
		super(msg);
	}


}
