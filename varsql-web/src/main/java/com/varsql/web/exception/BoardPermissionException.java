package com.varsql.web.exception;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.exception.VarsqlRuntimeException;

/**
*
* @FileName  : BoardPermissionException.java
* @Date      : 2020. 11. 12.
* @작성자      : ytkim
* @변경이력 :
* @프로그램 설명 : board permission exception
*/
public class BoardPermissionException extends VarsqlRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BoardPermissionException(String errorMessage) {
		super(VarsqlAppCode.EC_BOARD_PERMISSION, errorMessage);
	}

}
