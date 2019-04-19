package com.varsql.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.varsql.core.configuration.Configuration;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ContextLoaderListener.java
* @DESC		: 초기화할  Listener
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2015. 4. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class ContextLoaderListener  implements ServletContextListener{
	public void contextInitialized(ServletContextEvent event)
    {
		ServletContext sc = event.getServletContext();
		Configuration.getInstance();
	}	
				
	public void contextDestroyed(ServletContextEvent event){
		System.out.println("contextDestroyed-------------------------");
	}
}	