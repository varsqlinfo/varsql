package com.varsql.web.spring.config;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.varsql.configuration.ConnectionInfo;
import com.varsql.web.app.login.controller.LoginController;
import com.varsql.web.common.constants.ResourceConfigConstants;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: gain
* @NAME		: DataSourceConfig.java
* @DESC		: db 설정. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 3. 15.			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Configuration
public class DataSourceConfig {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private Environment env;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public DataSource gainDataSource() {
		
		ConnectionInfo ci = com.varsql.configuration.Configuration.getInstance().getVarsqlDB();
        
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(ci.getDriver());
		dataSource.setUrl(ci.getUrl());
		dataSource.setUsername(ci.getUsername());
		dataSource.setPassword(ci.getPassword());
		
		logger.debug("=================datasourceconfig info====================");
		logger.debug(" driver : {}", ci.getDriver());
		logger.debug(" url : {}",ci.getUrl());
		logger.debug(" username" ,ci.getUsername());
		logger.debug("=================datasourceconfig info====================");

		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager gainTransactionManager() {
		return new DataSourceTransactionManager(gainDataSource());
	}

	@Bean(name = "varsqlSessionFactoryBean")
	public SqlSessionFactoryBean gainSqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

		factoryBean.setDataSource(gainDataSource());
		factoryBean.setTypeAliasesPackage("com.varsql");

		factoryBean.setConfigLocation(
				pathMatchingResourcePatternResolver.getResource("classpath:/spring/etc/mybatis-env-setting.xml"));

		//Resource[] sql1 = applicationContext.getResources("classpath:/config/sql/*.xml");
		Resource[] sql2 = applicationContext.getResources("classpath:/com/varsql/web/configuration/*.xml");

		//factoryBean.setMapperLocations((Resource[]) ArrayUtils.addAll(sql1, sql2));
		factoryBean.setMapperLocations( sql2);
		return factoryBean;
	}

	@Bean(name = ResourceConfigConstants.APP_DB_RESOURCE)
	public SqlSessionTemplate varsqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
