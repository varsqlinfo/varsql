package com.varsql.web.dto.sql;

import com.varsql.core.common.code.VarsqlFileType;

import lombok.Getter;
import lombok.Setter;

/**
 * sql grid download info
* 
* @fileName	: SqlGridDownloadInfo.java
* @author	: ytkim
 */
@Setter
@Getter
public class SqlGridDownloadInfo{
	private VarsqlFileType exportType;

	private String fileName;
	
	private String headerInfo;

	private String gridData;
	
	public void setExportType(String exportType) {
		this.exportType = VarsqlFileType.getFileType(exportType);
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("exportType : ").append(exportType)
				.append(" ;; headerInfo : ").append(headerInfo)
				.toString();
	}
}
