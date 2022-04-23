package com.varsql.web.constants;

public enum MailType{
	RESET_PASSWORD("reset_pw")
	,INFO("info")
	,REGISTER("register");
	
	private String messageType;
	
	MailType(String msgType) {
		this.messageType = msgType;
	}
	
	public String getType() {
		return messageType;
	}
	
	
}