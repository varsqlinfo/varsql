package com.varsql.web.configuration;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.connection.ConnectionFactory;

import ch.qos.logback.classic.LoggerContext;

public class ShutdownHookConfiguration {

    public void destroy() {
    	
    	// quartz shutdown
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.shutdown(true);
			Thread.sleep(1000);
		}catch(Exception e) {
			System.out.println("quartz shutdown error : "+ e.getMessage());
		}
		
		// pool shutdown
		try {
			ConnectionFactory.poolShutdown();
		} catch (Exception e) {
			System.out.println("pool shutdown error : "+ e.getMessage());
		}
		
		// driver 해제
		JdbcDriverLoader.allDeregister();
		
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.stop();

    }
}