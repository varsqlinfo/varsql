package com.varsql.web.configuration;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.web.configuration.scheduler.QuartzConfig;
import com.varsql.web.constants.ResourceConfigConstants;

/**
 * -----------------------------------------------------------------------------
* @fileName		: JPAConfig.java
* @desc		: jpa 설정.
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@EnableTransactionManagement
@Import(value = {
   JPAConfigurer.class,
   QuartzConfig.class
})
public class DBConfigurer {

	private final Logger logger = LoggerFactory.getLogger(DBConfigurer.class);

    private DataSource mainDataSource;

	@PostConstruct
    public void initialize(){
		ConnectionInfo ci = Configuration.getInstance().getVarsqlDB();
		
		BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(ci.getJdbcDriverInfo().getDriverClass());
	    dataSource.setUrl(ci.getUrl());
	    dataSource.setUsername(ci.getUsername());
	    dataSource.setPassword(ci.getPassword());
	    dataSource.setInitialSize(ci.getInitialSize());
	    dataSource.setMaxTotal(ci.getMaxActive());
	    dataSource.setMinIdle(ci.getMinIdle());
	    dataSource.setMaxIdle(ci.getMaxIdle());
	    dataSource.setMaxWaitMillis(60000);
	    dataSource.setValidationQuery(ci.getValidationQuery());
	    dataSource.setTestWhileIdle(true);
	    

		logger.info("=================datasourceconfig info====================");
		logger.info(" driver : {}", ci.getJdbcDriverInfo().getDriverClass());
		logger.info(" url : {}",ci.getUrl());
		logger.info(" username : {} " ,ci.getUsername());
		logger.info("=================datasourceconfig info====================");

		mainDataSource = dataSource;
    }

    @Primary
    @Bean(name = ResourceConfigConstants.APP_DATASOURCE)
    public DataSource dataSource() {
        return mainDataSource;
    }
    
    @Primary
    @Bean(name = ResourceConfigConstants.APP_TRANSMANAGER)
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean(name = ResourceConfigConstants.APP_BATCH_TRANSMANAGER)
    public PlatformTransactionManager batchTransactionManager(final EntityManagerFactory emf) {
    	final JpaTransactionManager transactionManager = new JpaTransactionManager();
    	transactionManager.setEntityManagerFactory(emf);
    	return transactionManager;
    }
}