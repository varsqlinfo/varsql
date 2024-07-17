package com.varsql.web.configuration.websocket;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.varsql.web.constants.WebSocketConstants;
import com.vartech.common.utils.StringUtils;

/**
 * -----------------------------------------------------------------------------
* @fileName		: WebSocketBrokerConfig.java
* @desc		: web socket broker
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 10. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@ConditionalOnExpression("!'true'.equals('${varsql.runtime.test}')")
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
	
	private final Logger logger = LoggerFactory.getLogger(WebSocketBrokerConfig.class);
	
	public static final int MESSAGE_SIZE_LIMIT = 1024*1024*1; 
	
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	String [] destinationPrefixes= Arrays.asList(WebSocketConstants.Type.values()).stream().map(item -> item.getClientDestination()).toArray(String[]::new); 
    	
    	if(logger.isDebugEnabled()) {
	    	logger.debug("WebSocketBrokerConfig configureMessageBroker start");
	    	logger.debug("enableSimpleBroker : {}", StringUtils.join(destinationPrefixes));
	    	logger.debug("applicationDestinationPrefixes : {}", WebSocketConstants.APP_DESTINATION_PREFIX);
	    	logger.debug("userDestinationPrefix : {}", WebSocketConstants.USER_DESTINATION_PREFIX);
	    	logger.debug("WebSocketBrokerConfig configureMessageBroker end");
    	}
    	
        config.enableSimpleBroker(destinationPrefixes);
        config.setUserDestinationPrefix(WebSocketConstants.USER_DESTINATION_PREFIX);
        config.setApplicationDestinationPrefixes(WebSocketConstants.APP_DESTINATION_PREFIX);
        
    }
    
    /**
	 * buffer size 설정. 
	 */
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setSendTimeLimit(100 * 1000); // default : 10 * 1000
		registration.setSendBufferSizeLimit(MESSAGE_SIZE_LIMIT); // default : 512 * 1024
		registration.setDecoratorFactories(agentWebSocketHandlerDecoratorFactory());
	}
	
	public WebSocketHandlerDecoratorFactory agentWebSocketHandlerDecoratorFactory() {
		return new AgentWebSocketHandlerDecoratorFactory();
	}

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	
    	for(WebSocketConstants.Type type : WebSocketConstants.Type.values()) {
    		if(type.getEndPoint() != null) {
    			logger.debug("registerStompEndpoints : {}", type.getEndPoint());
    			registry.addEndpoint(type.getEndPoint())
				.setAllowedOriginPatterns("*")
    			.withSockJS()
    			.setStreamBytesLimit(MESSAGE_SIZE_LIMIT);
    		}
    	}
    }

}