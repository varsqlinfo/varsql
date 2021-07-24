package com.varsql.core.configuration.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public abstract class PreferencesAbstract implements PreferencesInterface {
	@JsonProperty(access = Access.WRITE_ONLY)
	@JacksonXmlProperty(isAttribute = true)
	private String key;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
