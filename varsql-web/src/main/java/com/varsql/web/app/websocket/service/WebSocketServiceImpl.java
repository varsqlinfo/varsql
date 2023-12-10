package com.varsql.web.app.websocket.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.WebSocketConstants;
import com.varsql.web.dto.websocket.MessageDTO;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
*
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: WebSocketServiceImpl.java
* @DESC		: web socket service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
 DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 10. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
*/
@Service
public class WebSocketServiceImpl{
	private final Logger logger = LoggerFactory.getLogger(WebSocketServiceImpl.class);
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	final private static String USER_PREFIX = WebSocketConstants.Type.USER.getClientDestination();
	
	@Async(ResourceConfigConstants.APP_WEB_SOCKET_TASK_EXECUTOR)
	public void sendUserMessage(MessageDTO msg , String... usrIds) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("websocket send message : {} , receivers : {}", msg, StringUtils.join(',',usrIds));
		}
		
		String jsonMsg = VartechUtils.objectToJsonString(msg); 
		
		for(String usrId : usrIds){
			this.simpMessagingTemplate.convertAndSend(USER_PREFIX+"." +usrId, jsonMsg);
		}
		
	}
}