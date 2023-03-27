package com.varsql.web.configuration;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;

import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateType;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.DBVenderType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.vartech.common.utils.StringUtils;

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
@EnableJpaAuditing(auditorAwareRef = "customAuditorAware")
@EnableJpaRepositories(basePackages = {"com.varsql.web.repository" ,"com.varsql.web.security.repository"}
,includeFilters ={
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value={Repository.class})
		,@ComponentScan.Filter(type = FilterType.REGEX, pattern="(service|controller|repository)\\.\\.*")
})
public class JPAConfigurer {

	private final Logger logger = LoggerFactory.getLogger(JPAConfigurer.class);

    @Autowired
    @Qualifier(ResourceConfigConstants.APP_DATASOURCE)
    private DataSource mainDataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws IOException {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mainDataSource);
        em.setPackagesToScan(new String[] {"com.varsql.web.model" });
        
        // native query 사용시 설정. 
//        Resource [] resources  = ResourceUtils.getPackageResources("classpath*:com/varsql/web/repository/xml/*.xml");
//        
//        if(resources.length > 0) {
//	        List<String> xmlList = new ArrayList<>();
//	        for (int i =0 ;i < resources.length;i++) {
//	        	Resource resource = resources[i];
//	        	if(resource != null && resource.exists()) {
//	        		xmlList.add("com/varsql/web/repository/xml/"+resource.getFile().getName());
//	        	}
//			}
//	        
//	        if(xmlList.size()> 0) {
//	        	em.setMappingResources(xmlList.toArray(new String[0]));
//	        }
//        }
        
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

	@Bean
	public JPAAuditorAware customAuditorAware() {
		return new JPAAuditorAware();
	}

    final Properties additionalProperties() {
    	
    	PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(com.varsql.core.configuration.Configuration.getInstance().getHibernateConfig());
    	
    	Properties properties = null;
		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();
		} catch (Exception e) {
			logger.error("Cannot load hibernate config path : {} , msg: {} " , com.varsql.core.configuration.Configuration.getInstance().getQuartzConfig(), e.getMessage(), e);
		}

        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
        hibernateProperties.setProperty("hibernate.default_batch_fetch_size", "10"); // join할때

        hibernateProperties.setProperty("org.hibernate.envers.audit_table_prefix", "ZAUD_");	// audit 테이블명 prefix
        hibernateProperties.setProperty("org.hibernate.envers.audit_table_suffix", "");		// suffix
        hibernateProperties.setProperty("org.hibernate.envers.store_data_at_delete", "true");	// delete 전에  모든 필드의 값을 쌓을때

        hibernateProperties.setProperty("hibernate.envers.autoRegisterListeners", "false");	// 감사 로그 등록 여부.
        
        DBVenderType dbType = DBVenderType.getDBType(Configuration.getInstance().getDbType());
        
        
        String dialect = properties.getProperty("hibernate.dialect");
        
        if(!StringUtils.isBlank(dialect)) {
        	hibernateProperties.setProperty("hibernate.dialect", dialect);
        }else {
	        if(DBVenderType.MYSQL.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.MySQLDialect.class.getName());
	        }else if(DBVenderType.SQLSERVER.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.SQLServerDialect.class.getName());
	        }else if(DBVenderType.ORACLE.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.Oracle12cDialect.class.getName());
	        }else if(DBVenderType.POSTGRESQL.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.PostgreSQL10Dialect.class.getName());
	        }else if(DBVenderType.CUBRID.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.CUBRIDDialect.class.getName());
	        }else if(DBVenderType.DB2.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.DB2Dialect.class.getName());
	        }else if(DBVenderType.MARIADB.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.MariaDBDialect.class.getName());
	        }else if(DBVenderType.TIBERO.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.Oracle12cDialect.class.getName());
	        }else if(DBVenderType.H2.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.H2Dialect.class.getName());
	        }else if(DBVenderType.SYBASE.equals(dbType)) {
	        	hibernateProperties.setProperty("hibernate.dialect", org.hibernate.dialect.SybaseDialect.class.getName());
	        }else {
	        	throw new Error("hibernate.dialect not load dbType : "+ dbType.getDbVenderName());
	        }
        }
        
        logger.info("hibernateProperties : {} ", hibernateProperties);
        
        return hibernateProperties;
    }

    @Bean
    public com.querydsl.sql.Configuration querydslConfiguration() {
        SQLTemplates templates = null;

        DBVenderType dbType = DBVenderType.getDBType(Configuration.getInstance().getDbType());

        if(DBVenderType.MYSQL.equals(dbType)) {
        	templates = com.querydsl.sql.MySQLTemplates.builder().build();
        }else if(DBVenderType.SQLSERVER.equals(dbType)) {
        	templates = com.querydsl.sql.SQLServerTemplates.builder().build();
        }else if(DBVenderType.ORACLE.equals(dbType)) {
        	templates = com.querydsl.sql.OracleTemplates.builder().build();
        }else if(DBVenderType.POSTGRESQL.equals(dbType)) {
        	templates = com.querydsl.sql.PostgreSQLTemplates.builder().build();
        }else if(DBVenderType.CUBRID.equals(dbType)) {
        	templates = com.querydsl.sql.CUBRIDTemplates.builder().build();
        }else if(DBVenderType.DB2.equals(dbType)) {
        	templates = com.querydsl.sql.DB2Templates.builder().build();
        }else if(DBVenderType.MARIADB.equals(dbType)) {
        	templates = com.querydsl.sql.MySQLTemplates.builder().build();
        }else if(DBVenderType.TIBERO.equals(dbType)) {
        	templates = OracleTemplates.builder().build();
        }else if(DBVenderType.H2.equals(dbType)) {
        	templates = com.querydsl.sql.H2Templates.builder().build();
        }else {
        	templates = com.querydsl.sql.MySQLTemplates.builder().build();
        }

        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());
        return configuration;
    }

    @Bean
    public SQLQueryFactory queryFactory() {
        return new SQLQueryFactory(querydslConfiguration(), new SpringConnectionProvider(mainDataSource));
    }
}