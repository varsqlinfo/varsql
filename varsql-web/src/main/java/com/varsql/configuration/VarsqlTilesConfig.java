package com.varsql.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

import com.varsql.app.common.view.VarsqlTilesView;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlTilesConfig.java
* @DESC		: 타일즈 설정. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 15.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
public class VarsqlTilesConfig extends VarsqlWebMvcConfigurerAdapter {

	/**
	 * Configure TilesConfigurer.
	 */
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions(new String[] { "classpath:/config/varsql-tiles.xml" });
		tilesConfigurer.setCheckRefresh(true);
		return tilesConfigurer;
	}
	
	@Bean
    public UrlBasedViewResolver tilesviewResolvers() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setViewClass(VarsqlTilesView.class);
        return viewResolver;
    }

}