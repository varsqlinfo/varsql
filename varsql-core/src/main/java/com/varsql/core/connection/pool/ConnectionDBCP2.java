package com.varsql.core.connection.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.exception.ConnectionFactoryException;

/**
 *
 * @FileName : ConnectionProvider.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 21.
 * @프로그램설명: pool 셋팅
 * @변경이력	:
 */
public class ConnectionDBCP2 extends AbstractConnectionPool{

	private static final String POOL_DRIVER = "org.apache.commons.dbcp2.PoolingDriver";

	protected final String DBCP_JDBC_PREFIX= "jdbc:apache:commons:dbcp:";

	private final Logger logger = LoggerFactory.getLogger(ConnectionDBCP2.class);

	PoolingDriver driver;

	public ConnectionDBCP2() {
		 //커넥션 풀을 제공하는 jdbc 드라이버를 등록.
        try {
			Class.forName(POOL_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("ConnectionDBCP2 ClassNotFoundException" ,e);
		}

        try {
			driver = (PoolingDriver) DriverManager.getDriver(DBCP_JDBC_PREFIX);
		} catch (SQLException e) {
			logger.error("ConnectionDBCP2 SQLException" ,e);
		}
	}

	@Override
	public void createDataSource(ConnectionInfo connInfo) throws ConnectionFactoryException {

		logger.info("!!!!!!!!!!!!!!!!! connection create start  !!!!!!!!!!!!!!!!!!!!!!!!!!!");

		try{
			String poolName = connInfo.getConnid();
			Properties properties = setConnectionOption(connInfo);


			Driver dbDriver = JdbcDriverLoader.getInstance().load(connInfo.getJdbcDriverInfo());

			ConnectionFactory connectionFactory;
			if(dbDriver==null) {
				connectionFactory = new DriverManagerConnectionFactory(connInfo.getUrl(), properties);
			}else {
				connectionFactory = new DriverConnectionFactory(dbDriver, connInfo.getUrl(), properties);
			}

			 //DBCP가 커넥션 풀에 커넥션을 보관할때 사용하는 PoolableConnectionFactory 생성
	        //실제로 내부적으로 커넥션을 담고있고 커넥션을 관리하는데 기능을 제공한다. ex)커넥션을 close하면 종료하지 않고 커넥션 풀에 반환
	        PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connectionFactory, null);

	        poolableConnFactory.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	        //커넥션이 유효한지 확인할때 사용하는 쿼리를 설정한다.
	        poolableConnFactory.setValidationQuery(connInfo.getValidationQuery());

	      //커넥션 풀의 설정 정보를 생성한다.
	        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	        //유휴 커넥션 검사 주기
	        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(1000L * 60L * 1L));
	        //풀에 있는 커넥션이 유효한지 검사 유무 설정
	        
	        if(connInfo.isTestWhileIdle()){
	        	poolConfig.setTestWhileIdle(true);
	        }
	        
	        //커넥션 최소갯수 설정
	        poolConfig.setMinIdle(connInfo.getMinIdle());
	        //커넥션 최대 갯수 설정
	        poolConfig.setMaxTotal(connInfo.getMaxActive());

	        //커넥션 풀 생성. 인자로는 위에서 생성한  PoolabeConnectionFactory와 GenericObjectPoolConfig를 사용한다.
	        GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnFactory, poolConfig);

	        //PoolabeConnectionFactory에도 커넥션 풀을 연결
	        poolableConnFactory.setPool(connectionPool);

	        //위에서 커넥션 풀 드라이버에 생성한 커넥션 풀을 등룍한다. 이름은 cp이다.
	        driver.registerPool(poolName, connectionPool);

	        logger.debug("poolName : {}", poolName);
			logger.debug("poolConfig : {}", poolConfig);


		}catch(Exception e){
			throw new ConnectionFactoryException(e.getMessage() , e);
		}
		logger.info("!!!!!!!!!!!!!!!!! connection create  end !!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	@Override
	public Connection getConnection(ConnectionInfo connInfo) throws ConnectionFactoryException {
		try{
			return DriverManager.getConnection(DBCP_JDBC_PREFIX+connInfo.getConnid());
		}catch(Exception e){
			String [] poolNames = driver.getPoolNames();
			String connid = connInfo.getConnid();

			if(Arrays.stream(poolNames).anyMatch(connid::equals)) {
				throw new ConnectionFactoryException(e.getMessage() , e);
			}

			throw new ConnectionFactoryException(VarsqlAppCode.EC_DB_POOL_CLOSE, e.getMessage(), e);
		}
	}

	public void poolShutdown(String nm) throws ConnectionFactoryException {
		try{
			logger.info("poolShutdown start : {}", nm);
			driver.closePool(nm);
			logger.info("poolName stop");
		}catch(Exception e){
			logger.error("poolName : {}", nm , e);
		}
	}

	@Override
	public synchronized void poolShutdown(ConnectionInfo connInfo) throws ConnectionFactoryException {
		poolShutdown(connInfo.getConnid());

	}

	/**
	 * 커넥션 옵션 정보 셋팅
	 * @Method Name  : setConnectionOption
	 * @Author : ytkim
	 * @Method desc :
	 * @History :
	 * @param ci
	 * @return
	 */
	private Properties setConnectionOption(ConnectionInfo ci) {

		Properties p =new Properties();

		p.setProperty("user", ci.getUsername());
		p.setProperty("password", ci.getPassword());

		Map customInfo =  ci.getConnectionOptions();

		if(customInfo != null){
			Iterator iter = customInfo.keySet().iterator();
			Object key = null;
			while(iter.hasNext()){
				key = iter.next();
				p.setProperty(String.valueOf(key), String.valueOf(customInfo.get(key)));
			}
		}

		return p;
	}
}
