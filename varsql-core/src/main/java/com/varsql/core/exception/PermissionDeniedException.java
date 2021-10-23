package com.varsql.core.exception;

import com.varsql.core.common.code.VarsqlAppCode;

/**
*
* @FileName  : PermissionDeniedException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : Permission Denied exception
*/
public class PermissionDeniedException extends VarsqlRuntimeException {

	private static final long serialVersionUID = 1L;


	public PermissionDeniedException(String errorMessage) {
		super(VarsqlAppCode.EC_PERMISSION, errorMessage);
	}
}