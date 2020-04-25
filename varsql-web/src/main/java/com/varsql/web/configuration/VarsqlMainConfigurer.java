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
		JPAConfigurer.class
		,VarsqlTilesConfigurer.class
        ,SecurityConfigurer.class
        ,ServiceConfigurer.class
})
public class VarsqlMainConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(VarsqlMainConfigurer.class);

    @Autowired
    private Environment env;

    /**
     * Application custom initialization code.
     * <p/>
     * Spring profiles can be configured with a system property
     * -Dspring.profiles.active=javaee
     * <p/>
     */
    @PostConstruct
    public void initApp() {
        LOG.debug("Looking for Spring profiles...");
        if (env.getActiveProfiles().length == 0) {
            LOG.info("No Spring profile configured, running with default configuration.");
        } else {
            for (String profile : env.getActiveProfiles()) {
                LOG.info("Detected Spring profile: {}", profile);
            }
        }
    }

    @Bean()
    public StringHttpMessageConverter converter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();

        converter.setSupportedMediaTypes(new ArrayList<MediaType>(){{
        	add(new MediaType("text", "plain", Charset.forName(VarsqlConstants.CHAR_SET)));
        }});;
        return converter;
    }
}
