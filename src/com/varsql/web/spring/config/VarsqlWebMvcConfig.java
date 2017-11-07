package com.varsql.web.spring.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.varsql.web.common.interceptor.DatabaseAuthInterceptor;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: gain
* @NAME		: GainWebMvcConfig.java
* @DESC		: web 설정. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 15.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
@Import(value = { 
       ValidatorConfig.class
})
public class VarsqlWebMvcConfig extends VarsqlWebMvcConfigurerAdapter {

    private static final int CACHE_PERIOD = 31556926; // one year
    
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    
    @PostConstruct
    public void init() {
    	requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }
    
    @Bean
    public ViewResolver viewResolver() {
        // Example: the 'info' view logical name is mapped to the file '/WEB-INF/jsp/info.jsp'
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/gain/");
        bean.setSuffix(".jsp");
        bean.setOrder(2);
        return bean;
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
    	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:nl/messages", "classpath:nl/label/label");
        messageSource.setDefaultEncoding("UTF-8");
        
        messageSource.setCacheMillis(180);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
    
	
	@Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(reloadableResourceBundleMessageSource());
    }
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Static ressources from both WEB-INF and webjars
        registry
            .addResourceHandler("/webstatic/**")
                .addResourceLocations("/webstatic/")
                .setCachePeriod(CACHE_PERIOD);
        registry
            .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(CACHE_PERIOD);
        
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // Serving static files using the Servlet container's default Servlet.
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        // add your custom formatters
    }
    /*
    /database/**" />
	<bean id="databaseAuthInterceptor" class="com.varsql.web.common.interceptor.DatabaseAuthInterceptor">
    */
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
	    //registry.addInterceptor(new RequestInterceptor());
    	//registry.addInterceptor(virtualPortalBaseInterceptor()).addPathPatterns("/portal");
    	
	    registry.addInterceptor(DatabaseAuthInterceptor()).addPathPatterns("/database/**","/sql/base/**");
	}
    
    @Bean
    public DatabaseAuthInterceptor DatabaseAuthInterceptor() {
        return new DatabaseAuthInterceptor();
    }

}
