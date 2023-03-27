package com.varsql.web.dto.sql;

import com.varsql.core.common.code.VarsqlFileType;

/**
 * sql grid download info
* 
* @fileName	: SqlGridDownloadInfo.java
* @author	: ytkim
 */
public class SqlGridDownloadInfo{
	private VarsqlFileType exportType;

	private String headerInfo;

	private String gridData;

	public SqlGridDownloadInfo(){
		super();
	}

	public VarsqlFileType getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = VarsqlFileType.getFileType(exportType);
	}

	public String getHeaderInfo() {
		return headerInfo;
	}

	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}

	public String getGridData() {
		return gridData;
	}

	public void setGridData(String gridData) {
		this.gridData = gridData;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("exportType : ").append(exportType)
				.append(" ;; headerInfo : ").append(headerInfo)
				.toString();
	}


}
