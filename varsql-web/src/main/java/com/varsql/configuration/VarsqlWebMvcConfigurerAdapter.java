package com.varsql.configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlWebMvcConfigurerAdapter.java
* @DESC		: base 웹 설정. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.varsql.app","com.varsql.auth"}
,includeFilters ={
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value={Controller.class,Service.class, Repository.class})
	,@ComponentScan.Filter(type = FilterType.REGEX, pattern="(service|controller|DAO)\\.\\.*")
})
public class VarsqlWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	
}
