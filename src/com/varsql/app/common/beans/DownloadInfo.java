package com.varsql.app.common.beans;

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
	
	private String exportType;
	
	private String fileName;
	
	private String content;
	
	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
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
