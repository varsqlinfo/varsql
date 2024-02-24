package com.varsql.web.constants;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vartech.common.utils.StringUtils;

public enum VarsqlURLInfo {
	FILE_DOWNLOAD("fileDownload" ,"/file/download?contId=#contId#&fileId=#fileId#")
	,DATA_DOWNLOAD("fileDownload" ,"/file/download?contId=#contId#&fileId=#fileId#")
	,DRIVER_FILE_DOWNLOAD("driverFileDownload" ,"/file/driverFileDownload?contId=#contId#&fileId=#fileId#");

	private String type;
	private String url;
	
	private final static String PARAM_REGEX = "#(.*?)#";
    private final static Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);

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
	
	public String getUrl(Map param) {
		Matcher matcher = PARAM_PATTERN.matcher(this.url);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String fieldName = matcher.group(1);
			String replacement = String.valueOf(param.get(fieldName));
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

	public String getType() {
		return type;
	}
}
