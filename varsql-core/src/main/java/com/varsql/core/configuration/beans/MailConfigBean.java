package com.varsql.core.configuration.beans;

import java.util.Properties;

import javax.persistence.Transient;

import lombok.Getter;

@Getter
public class MailConfigBean {
	
	private String host;
	
	private int port;
	
	private String fromUser;
	
	private String username;
	
	@Transient
	private String password;
	
	private String smtpAuth;
	
	private String smtpStarttlsEnable;
	
	private String transportProtocol;
	
	private String debug;
	
	public static MailConfigBean getMailConfigBean(Properties props) {
		MailConfigBean mailConfigBean = new MailConfigBean();
		
		mailConfigBean.host = props.getProperty("mail.host","");
		
		mailConfigBean.port = Integer.parseInt(props.getProperty("mail.port","-1"));
		
		mailConfigBean.username = props.getProperty("mail.username","");
		
		mailConfigBean.fromUser = props.getProperty("mail.fromuser","");
		
		mailConfigBean.password = props.getProperty("mail.password","");
		
		mailConfigBean.smtpAuth = props.getProperty("mail.smtp.auth","true");
		mailConfigBean.smtpStarttlsEnable = props.getProperty("mail.smtp.starttls.enable","true");
		
		
		mailConfigBean.transportProtocol = props.getProperty("mail.transport.protocol","smtp");
		mailConfigBean.debug = props.getProperty("mail.debug","false");
		
		return mailConfigBean;
	}
}
