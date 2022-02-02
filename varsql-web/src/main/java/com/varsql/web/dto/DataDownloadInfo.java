package com.varsql.web.dto;

import java.util.List;

import com.varsql.core.common.code.VarsqlFileType;

public class DataDownloadInfo {

	private String requid;
	
	private String conuid;

	private String schema;
	
	private String fileName;
	
	private String charset;
	
	private int limit;

	private VarsqlFileType exportType;
	
	private List<DownloadItemInfo> items;
	
	public String getRequid() {
		return requid;
	}

	public void setRequid(String requid) {
		this.requid = requid;
	}

	public String getConuid() {
		return conuid;
	}

	public void setConuid(String conuid) {
		this.conuid = conuid;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public VarsqlFileType getExportType() {
		return this.exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = VarsqlFileType.getFileType(exportType);
	}

	public List<DownloadItemInfo> getItems() {
		return items;
	}

	public void setItems(List<DownloadItemInfo> items) {
		this.items = items;
	}
	
}
