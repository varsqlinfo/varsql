package com.varsql.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.varsql.core.configuration.Configuration;

public class ContextLoaderListener  implements ServletContextListener{
	public void contextInitialized(ServletContextEvent event)
    {
		//DB연결이라면 초기화 코딩, 객체 컨텍스트에 담기.
		ServletContext sc = event.getServletContext();	        //이벤트에게 컨텍스트를 얻어온다
		
		Configuration.getInstance();
		
	}	
				
	public void contextDestroyed(ServletContextEvent event){
		System.out.println("contextDestroyed-------------------------");
	}
}	