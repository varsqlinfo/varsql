package com.varsql.configuration;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

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
@Configuration
@Import(value = { 
		 VarsqlSpringConfig.class
		,DataSourceConfig.class
		,VarsqlTilesConfig.class
		,VarsqlWebMvcConfig.class
        ,SecurityConfig.class
        ,ServiceConfig.class
})
public class VarsqlSpringMainConfig {

    private static final Logger LOG = LoggerFactory.getLogger(VarsqlSpringMainConfig.class);

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
}
