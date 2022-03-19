package com.varsql.core.connection.pool;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @FileName : ConnectionProvider.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 21.
 * @프로그램설명: pool 셋팅
 * @변경이력	:
 */
public class ConnectionHIKARI extends AbstractConnectionPool{

	private final Logger log = LoggerFactory.getLogger(ConnectionHIKARI.class);

	@Override
	public void createDataSource(ConnectionInfo connInfo) throws ConnectionFactoryException {
		try{
			log.info("!!!!!!!!!!!!!!!!! connection create start !!!!!!!!!!!!!!!!!!!!!!!!!!!");

			String poolName = connInfo.getConnid();

			HikariConfig config = new HikariConfig();

			config.setJdbcUrl( connInfo.getUrl() );
	        config.setUsername( connInfo.getUsername() );
	        config.setPassword(connInfo.getPassword() );
	        config.setMaximumPoolSize(connInfo.getMaxActive());
	        config.setConnectionTestQuery(connInfo.getValidationQuery());
	        config.setConnectionTimeout(connInfo.getConnectionTimeOut() *1000);

	        /*
	        config.addDataSourceProperty( "cachePrepStmts" , "true" );
	        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
	        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
	        */

	        config.setPoolName(poolName);

			log.info("openConnectionPool.Config : {}" ,config);

			poolShutdown(connInfo);

			connInfo.setDatasource(new HikariDataSource( config ));


			log.info("poolName : {}", poolName);
			log.info("connInfo : {}", connInfo);
			log.info("!!!!!!!!!!!!!!!!! connection end !!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}catch(Exception e){
			throw new ConnectionFactoryException(e.getMessage() , e);
		}
	}

	public Connection getConnection(ConnectionInfo ci) throws ConnectionFactoryException {
		try{
			return ci.getDatasource().getConnection();
		}catch(Exception e){
			throw new ConnectionFactoryException(e.getMessage() , e);
		}
	}

	public void poolShutdown(ConnectionInfo ci) throws ConnectionFactoryException {
		try{
			if(ci.getDatasource() != null){
				((HikariDataSource)ci.getDatasource()).close();
			}
		}catch(Exception e){
			log.error("poolShutdown " ,e.getMessage());
			log.error("poolShutdown " ,e);
		}
	}
}
