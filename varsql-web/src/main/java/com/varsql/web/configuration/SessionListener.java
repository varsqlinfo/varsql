package com.varsql.web.configuration;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
    	// 60*60 1hour
        event.getSession().setMaxInactiveInterval(3600);
    	//event.getSession().setMaxInactiveInterval(60*2);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        //System.out.println("==== Session is destroyed ====");
    }
}