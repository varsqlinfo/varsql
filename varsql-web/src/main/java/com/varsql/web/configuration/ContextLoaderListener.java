package com.varsql.web.configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
	public void contextInitialized(ServletContextEvent event)
    {
		//ServletContext sc = event.getServletContext();
		Configuration.getInstance();
	}

	public void contextDestroyed(ServletContextEvent event){
		System.out.println("contextDestroyed-------------------------");
	}
}