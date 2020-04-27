package com.varsql.web.configuration;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.varsql.web.common.interceptor.DatabaseAuthInterceptor;
import com.varsql.web.common.interceptor.LanguageInterceptor;
import com.varsql.web.constants.ViewPageConstants;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlWebMvcConfig.java
* @DESC		: web 설정.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 15.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
@ComponentScan(
	basePackages = { "com.varsql.web"},
	includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class, Service.class,Repository.class }),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "(service|controller|DAO|Repository)\\.\\.*")
})
@Import(value = {
       VarsqlMainConfigurer.class
})
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)  // "/error" request mapping 를 spring 기본을 사용하지 않기 위해 설정. 
public class VarsqlWebMvcConfigurer extends VarsqlWebConfigurer {

    private static final int CACHE_PERIOD = 31556926; // one year

    @PostConstruct
    public void init() {
    }

    @Bean
    public ViewResolver viewResolver() {
        // Example: the 'info' view logical name is mapped to the file '/WEB-INF/jsp/info.jsp'
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix(ViewPageConstants.VIEW_PREFIX);
        bean.setSuffix(ViewPageConstants.VIEW_SUFFIX);
        bean.setOrder(2);
        return bean;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * @method  : reloadableResourceBundleMessageSource
     * @desc : 매시지 처리
     * @author   : ytkim
     * @date   : 2020. 4. 21.
     * @return
     */
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
                .addResourceLocations("/webstatic/","classpath:/META-INF/resources/webstatic/")
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

    @Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(databaseAuthInterceptor()).addPathPatterns("/database/**","/sql/base/**");
	    registry.addInterceptor(languageInterceptor()).addPathPatterns("/**");
	}

    /**
     * @method  : databaseAuthInterceptor
     * @desc : database terceptor
     * @author   : ytkim
     * @date   : 2020. 4. 21.
     * @return
     */
    @Bean
    public DatabaseAuthInterceptor databaseAuthInterceptor() {
        return new DatabaseAuthInterceptor();
    }

    /**
     * @method  : languageInterceptor
     * @desc : 다국어 처리.
     * @author   : ytkim
     * @date   : 2020. 4. 21.
     * @return
     */
    @Bean
    public LanguageInterceptor languageInterceptor() {
    	return new LanguageInterceptor();
    }

    @Bean
    public LocaleResolver localeResolver()
    {
        final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
       // localeResolver.setDefaultLocale(new Locale("en", "US"));
        return localeResolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
       CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
       multipartResolver.setMaxUploadSize(com.varsql.core.configuration.Configuration.getInstance().getFileUploadSize());
       multipartResolver.setMaxUploadSizePerFile(com.varsql.core.configuration.Configuration.getInstance().getFileUploadSizePerFile());
       return multipartResolver;
    }
}
