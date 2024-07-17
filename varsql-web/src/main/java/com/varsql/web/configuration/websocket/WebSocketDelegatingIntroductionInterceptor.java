package com.varsql.web.configuration.websocket;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketDelegatingIntroductionInterceptor extends DelegatingIntroductionInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	protected Object doProceed(MethodInvocation mi) throws Throwable {
		if (mi.getMethod().getName().equals("afterConnectionEstablished")) {
			WebSocketSession session = (WebSocketSession) mi.getArguments()[0];
			session.setTextMessageSizeLimit(WebSocketBrokerConfig.MESSAGE_SIZE_LIMIT);
		}
		return super.doProceed(mi);
	}
}