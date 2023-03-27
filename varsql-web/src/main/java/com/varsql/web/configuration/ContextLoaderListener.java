package com.varsql.web.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.varsql.core.configuration.Configuration;

/**
 * -----------------------------------------------------------------------------
* @fileName		: ContextLoaderListener.java
* @desc		: varsql 초기화할  servlet listener
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class ContextLoaderListener  implements ServletContextListener{
	private ConfigurableApplicationContext webApplicationContext;
	
	public void contextInitialized(ServletContextEvent event)
    {
		System.out.println("init servlet context listener");
		ServletContext sc = event.getServletContext();
		Configuration.getInstance();
		
		webApplicationContext = (ConfigurableApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	public void contextDestroyed(ServletContextEvent event){
		System.out.println("servlet context listener destroyed");
		
		webApplicationContext.close();
		
		new ShutdownHookConfiguration().destroy();
		
	}
}