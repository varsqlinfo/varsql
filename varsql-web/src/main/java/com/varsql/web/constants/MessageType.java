package com.varsql.web.constants;

public enum MessageType{
	USER_BLOCK
	,USER_DB_BLOCK
	,NOTE
	,BATCH
	,TASK;
	
	public static MessageType fromString(String type) {
		for(MessageType messageType : MessageType.values()) {
			if(messageType.name().equals(type)) {
				return messageType;
			}
		}
		return null; 
	}
}