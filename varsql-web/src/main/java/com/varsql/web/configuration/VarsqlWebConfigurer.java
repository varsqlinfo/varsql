package com.varsql.web.configuration;

import javax.servlet.ServletContextListener;

import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.core.configuration.beans.web.CorsBean;


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
			factory.setContextPath(Configuration.getInstance().getContextPath());
		};
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

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
	    FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setForceEncoding(true);
	    characterEncodingFilter.setEncoding(VarsqlConstants.CHAR_SET);
	    registrationBean.setFilter(characterEncodingFilter);
	    registrationBean.setOrder(Integer.MIN_VALUE);
	    registrationBean.addUrlPatterns("/*");
	    return registrationBean;
	}
}
