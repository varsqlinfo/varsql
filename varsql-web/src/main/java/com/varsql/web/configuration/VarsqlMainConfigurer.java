package com.varsql.web.configuration;

import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.varsql.core.common.constants.VarsqlConstants;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlSpringMainConfig.java
* @DESC		: spring 설정.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 15.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */


@Import(value = {
	DBConfigurer.class
	,VarsqlTilesConfigurer.class
    ,SecurityConfigurer.class
    ,ServiceConfigurer.class
    ,JacksonConfigurer.class
    ,MailConfigurer.class
})
public class VarsqlMainConfigurer {

	private final Logger LOG = LoggerFactory.getLogger(VarsqlMainConfigurer.class);

    @Bean()
    public StringHttpMessageConverter converter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();

        converter.setSupportedMediaTypes(new ArrayList<MediaType>(){
			private static final long serialVersionUID = 1735295099855086282L;

		{
        	add(new MediaType("text", "plain", Charset.forName(VarsqlConstants.CHAR_SET)));
        }});;
        return converter;
    }
}
