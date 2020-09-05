package com.varsql.web.configuration;
import java.sql.Connection;
import java.util.Properties;

import javax.inject.Provider;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateType;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBType;
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
@PropertySource({ "classpath:persistence-h2.properties" })
@EnableJpaAuditing(auditorAwareRef = "customAuditorAware")
@EnableJpaRepositories(basePackages = {"com.varsql.web.repository" ,"com.varsql.web.security.repository"}
,includeFilters ={
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value={Repository.class})
		,@ComponentScan.Filter(type = FilterType.REGEX, pattern="(service|controller|DAO|Repository)\\.\\.*")
})
public class JPAConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(JPAConfigurer.class);

    @Autowired
    private Environment env;

    public JPAConfigurer() {
        super();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] {"com.varsql.web.model" });

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource() {

    	ConnectionInfo ci = Configuration.getInstance().getVarsqlDB();

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

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

	@Bean
	public JPAAuditorAware customAuditorAware() {
		return new JPAAuditorAware();
	}

    final Properties additionalProperties() {

        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
        hibernateProperties.setProperty("hibernate.default_batch_fetch_size", "10"); // join할때

        hibernateProperties.setProperty("org.hibernate.envers.audit_table_prefix", "ZAUD_");	// audit 테이블명 prefix
        hibernateProperties.setProperty("org.hibernate.envers.audit_table_suffix", "");		// suffix
        hibernateProperties.setProperty("org.hibernate.envers.store_data_at_delete", "true");	// delete 전에  모든 필드의 값을 쌓을때

        hibernateProperties.setProperty("hibernate.envers.autoRegisterListeners", "false");	// 감사 로그 등록 여부.

        return hibernateProperties;
    }


    @Bean
    public com.querydsl.sql.Configuration querydslConfiguration() {
        SQLTemplates templates = H2Templates.builder().build();

        String dbType = Configuration.getInstance().getDbType();
        if(DBType.MYSQL.equals(dbType)) {
        	templates = MySQLTemplates.builder().build();
        }else if(DBType.MSSQL.equals(dbType)) {
        	templates = SQLServerTemplates.builder().build();
        }else if(DBType.ORACLE.equals(dbType)) {
        	templates = OracleTemplates.builder().build();
        }else if(DBType.POSTGRESQL.equals(dbType)) {
        	templates = com.querydsl.sql.PostgreSQLTemplates.builder().build();
        }else if(DBType.CUBRID.equals(dbType)) {
        	templates = com.querydsl.sql.CUBRIDTemplates.builder().build();
        }else if(DBType.DB2.equals(dbType)) {
        	templates = com.querydsl.sql.DB2Templates.builder().build();
        }else if(DBType.MARIADB.equals(dbType)) {
        	templates = com.querydsl.sql.MySQLTemplates.builder().build();
        }else if(DBType.TIBERO.equals(dbType)) {
        	templates = com.querydsl.sql.OracleTemplates.builder().build();
        }else if(DBType.H2.equals(dbType)) {
        	templates = com.querydsl.sql.H2Templates.builder().build();
        }else {
        	templates = com.querydsl.sql.H2Templates.builder().build();
        }

        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());
        return configuration;
    }

    @Bean
    public SQLQueryFactory queryFactory() {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource());
        return new SQLQueryFactory(querydslConfiguration(), provider);
    }
}