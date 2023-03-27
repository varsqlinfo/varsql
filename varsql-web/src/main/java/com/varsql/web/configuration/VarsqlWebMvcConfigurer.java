package com.varsql.web.configuration;

import javax.annotation.PostConstruct;

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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlSpringBeanUtils;
import com.varsql.core.configuration.Constants;
import com.varsql.web.common.interceptor.DatabaseAuthInterceptor;
import com.varsql.web.common.interceptor.DatabaseBoardAuthInterceptor;
import com.varsql.web.common.interceptor.LanguageInterceptor;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.constants.ViewResourceConstants;

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
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class, Service.class, Repository.class, Component.class}),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "(service|controller|DAO|Repository)\\.\\.*")
	}
	,excludeFilters = {
			@ComponentScan.Filter(type = FilterType.REGEX, pattern = "(configuration)\\.*")	
	}
)
@Import(value = {
       VarsqlMainConfigurer.class
})
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)  // "/error" request mapping 를 spring 기본을 사용하지 않기 위해 설정.
public class VarsqlWebMvcConfigurer extends VarsqlWebConfigurer {

    private static final int CACHE_PERIOD = 31556926; // one year

    @PostConstruct
    public void init() {
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
    	
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(ViewResourceConstants.VIEW_PREFIX);
        resolver.setSuffix(ViewResourceConstants.VIEW_SUFFIX);
        resolver.setOrder(2);
        //resolver.setViewClass(JstlView.class);
        registry.viewResolver(resolver);
    }

    /**
     * @method  : reloadableResourceBundleMessageSource
     * @desc : 매시지 처리
     * @author   : ytkim
     * @date   : 2020. 4. 21.
     * @return
     */
    @Bean(name = ResourceConfigConstants.APP_MESSAGE_SOURCE)
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
    	AppResourceMessageBundleSource messageSource = new AppResourceMessageBundleSource();
        messageSource.setBasenames("classpath:nl/messages", "classpath:nl/label/label",  "classpath:nl/client/scriptMessages");
        messageSource.setDefaultEncoding(VarsqlConstants.CHAR_SET);

        if(VarsqlUtils.isRuntimelocal()) {
        	messageSource.setCacheSeconds(3);
        }
        messageSource.setFallbackToSystemLocale(false);
        
        // message 없을때 에러 코드 표시 여부.
        //messageSource.setUseCodeAsDefaultMessage(true);
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

	    registry.addInterceptor(databaseBoardAuthInterceptor()).addPathPatterns(new String[] { "/board/**" });

	    registry.addInterceptor(languageInterceptor()).excludePathPatterns(SecurityConfigurer.WEB_RESOURCES).addPathPatterns("/**");
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

    @Bean
	public DatabaseBoardAuthInterceptor databaseBoardAuthInterceptor() {
		return new DatabaseBoardAuthInterceptor();
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
        return localeResolver;
    }

    @Bean
	public VarsqlSpringBeanUtils varsqlSpringBeanUtils() {
		return new VarsqlSpringBeanUtils();
	}

    @Bean
    public MultipartResolver multipartResolver() {
       CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
       multipartResolver.setMaxUploadSize(com.varsql.core.configuration.Configuration.getInstance().getFileUploadSize());
       multipartResolver.setMaxUploadSizePerFile(com.varsql.core.configuration.Configuration.getInstance().getFileUploadSizePerFile());
       multipartResolver.setDefaultEncoding(VarsqlConstants.CHAR_SET);
       multipartResolver.setMaxInMemorySize(com.varsql.core.configuration.Configuration.getInstance().getFileUploadMaxInMemorySize());
       return multipartResolver;
    }
}
