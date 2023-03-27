package com.varsql.web.dto.file;

import com.varsql.core.common.code.VarsqlFileType;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DownloadInfo.java
* @DESC		:
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 9. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DownloadInfo {

	private VarsqlFileType exportType;

	private String fileName;

	private String content;

	public VarsqlFileType getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = VarsqlFileType.getFileType(exportType);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
