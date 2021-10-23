package com.varsql.core.configuration.beans.pref;

import com.vartech.common.utils.StringUtils;

public class PropItem {
	private String key;
	private String name;
	private String code;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		if(code != null) {
			this.code = StringUtils.trim(code);
		}else {
			this.code = code;
		}
	}
}