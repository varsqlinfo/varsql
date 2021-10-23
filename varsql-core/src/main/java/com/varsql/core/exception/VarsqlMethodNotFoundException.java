package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

/**
 *
 * @FileName  : VarsqlMethodNotFoundException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlMethodNotFoundException extends VarsqlException {

	private static final long serialVersionUID = 1L;

	public VarsqlMethodNotFoundException(String errorMessage) {
		super(VarsqlAppCode.EC_METHOD_NOT_FOUND, errorMessage);
	}

}
