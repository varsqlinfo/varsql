package com.varsql.web.dto.sql;

import com.varsql.core.db.valueobject.DatabaseParamInfo;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlGridDownloadInfo.java
* @DESC		: sql grid download info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 10. 12. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlGridDownloadInfo extends DatabaseParamInfo{
	private String exportType;

	private String headerInfo;

	private String gridData;

	public SqlGridDownloadInfo(){
		super();
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
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
