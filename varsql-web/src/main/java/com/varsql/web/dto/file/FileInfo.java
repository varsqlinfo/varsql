package com.varsql.web.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileInfo.java
* @DESC		: file bean 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 29. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
public class FileInfo {
	private String fileDiv;
	private String fileContId;
	private String fileId;
	private String fileFieldName;
    private String fileName;
    private String orginFileName;
    private String fileDownloadUri;
    private String fileType;
    private String filePath;
    private String fileExt;
    private String userId;
    private long fileSize;
    
    @Builder
    public FileInfo(String fileDiv, String fileContId, String fileId, String fileFieldName, String fileName, String orginFileName, String fileDownloadUri, String fileType, String filePath
    		, String fileExt, String userId, long fileSize) {
    	this.fileDiv = fileDiv;
    	this.fileContId = fileContId;
    	this.fileId = fileId;
    	this.fileFieldName = fileFieldName;
    	this.fileName = fileName;
    	this.orginFileName = orginFileName;
    	this.fileDownloadUri = fileDownloadUri;
    	this.fileType = fileType;
    	this.filePath = filePath;
    	this.fileExt = fileExt;
    	this.userId = userId;
    	this.fileSize = fileSize;
	}
	
}
