package com.varsql.web.app.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.varsql.web.app.websocket.service.WebSocketServiceImpl;
import com.varsql.web.common.controller.AbstractController;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: WebSocketController.java
* @DESC		: web socket
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 10. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/websocket")
public class WebSocketController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

	@Autowired
	private WebSocketServiceImpl webSocketServiceImpl;

	
}
