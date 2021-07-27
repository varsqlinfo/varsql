package com.varsql.core.configuration.beans.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.varsql.core.common.code.VarsqlSsoType;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SsoBean {
	@JacksonXmlProperty(localName = "token")
	private String token;

	@JacksonXmlProperty(localName = "mode")
	private VarsqlSsoType mode = VarsqlSsoType.NEVER;

	@JacksonXmlProperty(localName = "componentName")
	private String componentName;

	private SsoBean(){}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public VarsqlSsoType getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = VarsqlSsoType.of(mode);
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("token : ").append(token)
				.append(" , mode : ").append(mode)
				.append(" , componentName : ").append(componentName)
				.toString();
	}
}
