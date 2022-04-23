package com.varsql.core.common.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * main send info
 * @author ytkim
 *
 */
@Getter
@Setter
public class MailInfo {

	private String from;

	private String to;

	private String cc;

	private String bcc;

	private String subject;

	private String content;

	private String contentType;
	
	private String charset;

	@Builder
	public MailInfo(String from, String to, String subject, String content, String cc, String bcc, String contentType, String charset) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.cc = cc;
		this.bcc = bcc;
		this.contentType = contentType;
		this.charset = charset;
	}
	
	public String getContentType() {
		if(this.contentType == null) {
			return "text/plain";
		}
		return this.contentType;
	}
	
}
