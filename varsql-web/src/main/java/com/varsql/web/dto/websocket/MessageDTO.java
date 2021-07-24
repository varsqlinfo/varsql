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
	private String message;
	private Map<String,String> item;

	@Builder
	public MessageDTO(MessageType type, String message) {
		this.type = type;
		this.message = message;
	}

	public MessageDTO addItem(String key, String value) {
		if(item==null) {
			item = new HashMap<String,String>();
		}
		item.put(key, value);

		return this;
	}
}