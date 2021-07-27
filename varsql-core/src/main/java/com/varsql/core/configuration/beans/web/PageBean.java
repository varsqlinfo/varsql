package com.varsql.core.configuration.beans.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.vartech.common.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class PageBean{

	@JacksonXmlProperty(localName = "pageCsrf")
	private String pageCsrf="/error/error403csrf";

	@JacksonXmlProperty(localName = "page403")
	private String page403 ="/error/error403";

	@JacksonXmlProperty(localName = "page404")
	private String page404 = "/error/error403";

	@JacksonXmlProperty(localName = "page500")
	private String page500 = "/error/error500";

	@JacksonXmlProperty(localName = "help")
	private String pageHelp = "/error/helpPage";

	private PageBean(){}

	public String getPageCsrf() {
		return pageCsrf;
	}
	public void setPageCsrf(String pageCsrf) {
		this.pageCsrf = StringUtils.defaultString(pageCsrf, this.pageCsrf);
	}
	public String getPage403() {
		return page403;
	}
	public void setPage403(String page403) {
		this.page403 = StringUtils.defaultString(pageCsrf, this.page403);
	}
	public String getPage404() {
		return page404;
	}
	public void setPage404(String page404) {
		this.page404 =  StringUtils.defaultString(pageCsrf, this.page404);;
	}
	public String getPage500() {
		return page500;
	}
	public void setPage500(String page500) {
		this.page500 =  StringUtils.defaultString(pageCsrf, this.page500);;
	}
	public String getPageHelp() {
		return pageHelp;
	}
	public void setPageHelp(String pageHelp) {
		this.pageHelp =  StringUtils.defaultString(pageCsrf, this.pageHelp);;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("pageCsrf : ").append(pageCsrf)
				.append(" , page403 : ").append(page403)
				.append(" , page404 : ").append(page404)
				.append(" , page500 : ").append(page500)
				.append(" , pageHelp : ").append(pageHelp)
				.toString();
	}
}