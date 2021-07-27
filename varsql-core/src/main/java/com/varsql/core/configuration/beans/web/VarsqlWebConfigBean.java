package com.varsql.core.configuration.beans.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "web-config")
public class VarsqlWebConfigBean {
	
	@JacksonXmlProperty(localName = "sso")
	private SsoBean  sso;
	
	@JacksonXmlProperty(localName = "page")
	private PageBean page;

	public SsoBean getSso() {
		return sso;
	}

	public void setSso(SsoBean sso) {
		this.sso = sso;
	}

	public PageBean getPage() {
		return page;
	}

	public void setPage(PageBean page) {
		this.page = page;
	} 
}

