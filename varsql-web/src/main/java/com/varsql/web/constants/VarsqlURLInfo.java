package com.varsql.web.constants;

import com.vartech.common.utils.StringUtils;

public enum VarsqlURLInfo {
	FILE_DOWNLOAD("fileDownload" ,"/file/download?contId=#contId#&fileId=#fileId#")
	,DATA_DOWNLOAD("fileDownload" ,"/file/download?contId=#contId#&fileId=#fileId#");

	private String type;
	private String url;
	
	VarsqlURLInfo(String type, String url) {
		this.type = type;
		this.url = url;
	}
	
	public static VarsqlURLInfo get(String type) {
		if(StringUtils.isBlank(type)) return null; 
		
		for(VarsqlURLInfo info : VarsqlURLInfo.values()) {
			if(info.type.equals(type)) {
				return info;
			}
		}
		
		return null; 
	}

	public String getUrl() {
		return url;
	}

	public String getType() {
		return type;
	}
}
