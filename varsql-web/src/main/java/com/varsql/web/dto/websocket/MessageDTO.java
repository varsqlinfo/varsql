package com.varsql.web.dto.websocket;

import java.util.HashMap;
import java.util.Map;

import com.varsql.web.constants.MessageType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: MessageDTO.java
* @DESC		: web socket message
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 10. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
public class MessageDTO{

	private MessageType type;
	private String title;
	private String message;
	private String[] recvIds;
	private Map<String,String> item;

	@Builder
	public MessageDTO(MessageType type, String title, String message, String[] recvIds) {
		this.type = type;
		this.title = title;
		this.message = message;
		this.recvIds = recvIds;
	}

	public MessageDTO addItem(String key, String value) {
		if(item==null) {
			item = new HashMap<String,String>();
		}
		item.put(key, value);

		return this;
	}
}