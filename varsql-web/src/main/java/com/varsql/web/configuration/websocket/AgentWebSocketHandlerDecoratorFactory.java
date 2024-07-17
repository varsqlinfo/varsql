package com.varsql.web.configuration.websocket;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 *  websocket set limit size  설정 추가. 
* 
* @fileName	: AgentWebSocketHandlerDecoratorFactory.java
* @author	: ytkim
 */
public class AgentWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {
	@Override
	public WebSocketHandler decorate(WebSocketHandler handler) {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setTargetClass(AopUtils.getTargetClass(handler));
		proxyFactory.setTargetSource(new SingletonTargetSource(handler));
		proxyFactory.addAdvisor(new DefaultIntroductionAdvisor(new WebSocketDelegatingIntroductionInterceptor()));
		proxyFactory.setOptimize(true);
		proxyFactory.setExposeProxy(true);
		return (WebSocketHandler) proxyFactory.getProxy();
	}
}