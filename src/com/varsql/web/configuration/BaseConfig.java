package com.varsql.web.configuration;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.varsql.configuration.ConnectionInfo;
 
@Configuration
public class BaseConfig {
     
    @Bean(destroyMethod="close")
    public DataSource dataSource(){
    	ConnectionInfo ci = com.varsql.configuration.Configuration.getInstance().getVarsqlDB();
    	
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(ci.getDriver());
        dataSource.setUrl(ci.getUrl());
        dataSource.setUsername(ci.getUsername());
        dataSource.setPassword(ci.getPassword());
        dataSource.setDefaultAutoCommit(false);
     
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}