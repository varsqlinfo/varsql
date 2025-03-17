package com.varsql.core.configuration.beans.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "web-config")
public class VarsqlWebConfigBean {
	
	@JacksonXmlProperty(localName = "sso")
	private SsoBean  sso;
	
	@JacksonXmlProperty(localName = "page")
	private PageBean page;
	
	@JacksonXmlProperty(localName = "cors")
	private CorsBean cors;

	 
}

