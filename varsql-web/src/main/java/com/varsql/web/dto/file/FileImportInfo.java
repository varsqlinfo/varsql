package com.varsql.web.dto.file;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileImportInfo.java
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
@ToString
public class FileImportInfo {
	
	@NotEmpty
	private String importType;
	
	@NotEmpty
	private String fileIds;
	
	@NotEmpty
	private String conuid;
	
	private String charset;

}
