package com.varsql.web.dto.file;

import com.varsql.core.common.code.VarsqlAppCode;
import com.vartech.common.constants.RequestResultCode;

import lombok.Getter;
import lombok.Setter;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileImportResult.java
* @DESC		:
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 9. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
public class FileImportResult {
	private String fileName;

	private String message;

	private long resultCount;

	private VarsqlAppCode resultCode = VarsqlAppCode.SUCCESS;

}
