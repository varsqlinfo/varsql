package com.varsql.web.constants;

public enum MessageType{
	USER_BLOCK("userBlock")
	,USER_DB_BLOCK("dbBlock")
	,NOTE("note");
	
	private String messageType;
	
	MessageType(String msgType) {
		this.messageType = msgType;
	}
	
	public String getMessageType() {
		return messageType;
	}
	
	
}