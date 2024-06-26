package com.varsql.web.configuration.scheduler;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.varsql.web.app.scheduler.InitJobBean;
import com.varsql.web.app.scheduler.listener.JobsListenerImpl;
import com.varsql.web.app.scheduler.listener.TriggerListenerImpl;
import com.varsql.web.constants.ResourceConfigConstants;

@Conditional(SchedulerCondition.class)
public class QuartzConfig {

	private static final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);

	@Autowired
	@Qualifier(ResourceConfigConstants.APP_DATASOURCE)
	private DataSource dataSource;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private ApplicationContext applicationContext;
	

	@Bean(name=ResourceConfigConstants.APP_SCHEDULER)
	public SchedulerFactoryBean schedulerFactory() {
		logger.info("SchedulerFactoryBean created!");

		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		schedulerFactoryBean.setJobFactory(jobFactory);
		schedulerFactoryBean.setTransactionManager(transactionManager);
		schedulerFactoryBean.setDataSource(dataSource);
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		schedulerFactoryBean.setAutoStartup(true);
		schedulerFactoryBean.setGlobalJobListeners(globalJobListeners());
		schedulerFactoryBean.setGlobalTriggerListeners(globalTriggerListenerImpl());
		
		schedulerFactoryBean.setQuartzProperties(quartzProperties());

		return schedulerFactoryBean;
	}
	
	@Bean
	public JobsListenerImpl globalJobListeners(){
		return new JobsListenerImpl();
	}
	
	@Bean
	public TriggerListenerImpl globalTriggerListenerImpl(){
		return new TriggerListenerImpl();
	}
	
	@Bean
	public Properties quartzProperties() {
		
		Properties properties = null;
		try {
			
			logger.debug("quartz property : {}" , com.varsql.core.configuration.Configuration.getInstance().getQuartzConfig());
			
			PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
			
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
			Resource resource = resolver.getResource(com.varsql.core.configuration.Configuration.getInstance().getQuartzConfig());
			
			propertiesFactoryBean.setLocation(resource);
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();
			
			
			//path 수정 할것. 
			
		} catch (Exception e) {
			logger.warn("Cannot load quartz config path  msg: {} ", e.getMessage(), e);
		}
		return properties;
	}
	
	@Bean(initMethod="init")
	public InitJobBean initJobBean() {
	    return new InitJobBean();
	}
}