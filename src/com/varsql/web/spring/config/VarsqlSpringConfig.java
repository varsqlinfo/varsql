package com.varsql.web.spring.config;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: gain
* @NAME		: GainSpringConfig.java
* @DESC		: spring bean 설정. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 15.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
public class VarsqlSpringConfig {

	@Bean()
    public StringHttpMessageConverter converter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		
        converter.setSupportedMediaTypes(new ArrayList<MediaType>(){{
        	add(new MediaType("text", "plain", Charset.forName("UTF-8")));
        }});;
        return converter;
    }
	
}