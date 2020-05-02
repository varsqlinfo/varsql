package com.varsql.web.configuration;

import javax.servlet.ServletContextListener;

import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlWebConfigurer.java
* @desc		:
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 9. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */

public class VarsqlWebConfigurer implements WebMvcConfigurer {
	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {
			factory.setContextPath("/vsql");
		};
	}
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


	
	@Bean
	public ServletListenerRegistrationBean<ServletContextListener> listenerRegistrationBean() {
		ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<>();
		bean.setListener(new ContextLoaderListener());
		return bean;
	}

	@Bean
	public ErrorPageRegistrar errorPageRegistrar(){
	    return new VarsqlErrorPageRegistrar();
	}
}
