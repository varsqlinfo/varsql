package com.varsql.web.configuration.websocket;
import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import com.varsql.web.constants.WebSocketConstants;

/**
 * -----------------------------------------------------------------------------
* @fileName		: WebSocketSecurityConfig.java
* @desc		: web event
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 10. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    	
    	String[] destMatchers = (String[]) Arrays.asList(WebSocketConstants.Type.values()).stream().map(item -> item.getDestMatcher()).toArray(String[]::new);
        messages
                .simpDestMatchers(destMatchers).authenticated()
                .anyMessage().authenticated();
    }
}