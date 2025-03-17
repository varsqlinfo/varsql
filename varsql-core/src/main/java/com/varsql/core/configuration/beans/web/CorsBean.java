package com.varsql.core.configuration.beans.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class CorsBean {
	public static final String END_SPLIT_CHAR = ",";
	
	@JacksonXmlProperty(localName = "allowedOrigins")
	private String allowedOrigins;
	
	@JacksonXmlProperty(localName = "allowedMethods")
	private String allowedMethods;
	
	@JacksonXmlProperty(localName = "allowedHeaders")
	private String allowedHeaders;
	
	
	@JacksonXmlProperty(localName = "allowCredentials")
	private boolean allowCredentials;
	
	@JacksonXmlProperty(localName = "maxAge")
	private int maxAge;

	public String getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(String allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public String getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(String allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public String getAllowedHeaders() {
		return allowedHeaders;
	}

	public void setAllowedHeaders(String allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}

	public boolean isAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	
	
	
	
	
}
