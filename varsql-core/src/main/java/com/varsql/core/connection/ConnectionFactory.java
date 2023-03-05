package com.varsql.core.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.VarsqlSpringBeanUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.pool.ConnectionPoolInterface;
import com.varsql.core.connection.pool.PoolStatus;
import com.varsql.core.connection.pool.PoolType;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.exception.ConnectionFactoryException;

/**
 *
 * @FileName  : ConnectionFactory.java
 * @프로그램 설명 : Connection Factory
 * @Date      : 2018. 2. 13.
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class ConnectionFactory implements ConnectionContext{

	private final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	private PoolType connectionPoolType = PoolType.DBCP2;

	private final ConcurrentHashMap<String, ConnectionInfo> connectionConfig = new ConcurrentHashMap<String, ConnectionInfo>();

	private final ConcurrentHashMap<String, Boolean> connectionShutdownInfo = new ConcurrentHashMap<String, Boolean>();

	private ConnectionInfoDao connectionInfoDao;

	private ConnectionFactory() {
		init();
	}

	@Override
	public synchronized Connection getConnection(String connid) throws ConnectionFactoryException  {
		return getConnection(connid ,true);
	}

	public synchronized Connection getConnection(String connid , boolean returnFlag) throws ConnectionFactoryException  {
		ConnectionInfo connInfo  = getConnectionInfo(connid);

		if(connInfo != null){
			if(isShutdown(connid))
				;

			return connectionPoolType.getPoolBean().getConnection(connInfo);
		}

		throw new ConnectionFactoryException(" null connection infomation : "+ connid);
	}

	public synchronized void createPool(ConnectionInfo connInfo) throws SQLException, ConnectionFactoryException  {
		connectionPoolType.getPoolBean().createDataSource(connInfo);
		connectionConfig.put(connInfo.getConnid(), connInfo);
	}

	/**
	 *
	 * @Method Name  : getConnectionInfo
	 * @Method 설명 : connection info
	 * @작성일   : 2018. 2. 13.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ConnectionInfo getConnectionInfo(String connid) throws ConnectionFactoryException{
		if(connectionConfig.containsKey(connid)){
			return connectionConfig.get(connid);
		}else{
			return createConnectionInfo(connid);
		}
	}

	/**
	 *
	 * @Method Name  : createConnectionInfo
	 * @Method 설명 : connection info 생성.
	 * @작성일   : 2018. 2. 13.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @return
	 * @throws Exception
	 */
	private synchronized ConnectionInfo createConnectionInfo(String connid) throws ConnectionFactoryException{
		try {
			ConnectionInfo connInfo = connectionInfoDao.getConnectionInfo(connid);
			getPoolBean().createDataSource(connInfo);
			this.connectionConfig.put(connid, connInfo);
			return connInfo;
		} catch (Exception e) {
			this.logger.error("createConnectionInfo error ", e);
			if(e instanceof ConnectionFactoryException) {
				throw (ConnectionFactoryException)e;
			}
			
			throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR ,"createConnectionInfo error : [" + connid + "]", e);
		}
	}


	/**
	 *
	 * @Method Name  : resetConnectionPool
	 * @Method 설명 : connection pool reset
	 * @작성일   : 2016. 9. 26.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param connid
	 * @throws SQLException
	 * @throws Exception
	 */
	public synchronized void resetConnectionPool(String connid) throws SQLException, ConnectionFactoryException  {
		poolShutdown(connid);
		connectionShutdownInfo.remove(connid);
		createConnectionInfo(connid);
	}

	/**
	 * @method  : poolShutdown
	 * @desc : 연결 해제 .
	 * @author   : ytkim
	 * @date   : 2020. 5. 24.
	 * @param connid
	 * @throws SQLException
	 * @throws ConnectionFactoryException
	 */
	public synchronized void poolShutdown(String connid) throws SQLException, ConnectionFactoryException  {

		logger.error("db pool shutdown : {}" , connid);

		if(connectionConfig.containsKey(connid)){
			ConnectionFactory.getInstance().getPoolBean().poolShutdown( connectionConfig.get(connid));
		}
		closeMataDataSource(connid);
		connectionShutdownInfo.put(connid, true);
	}
	
	public static void poolShutdown() throws SQLException, ConnectionFactoryException  {
		getInstance().allPoolShutdown();
	}
	
	private synchronized void allPoolShutdown() throws SQLException, ConnectionFactoryException  {
		
		if(connectionConfig.size() > 0) {
			for(Entry<String, ConnectionInfo> connEntry: connectionConfig.entrySet()) {
				String connid = connEntry.getKey();
				getInstance().getPoolBean().poolShutdown( connectionConfig.get(connid));
				closeMataDataSource(connid);
				connectionShutdownInfo.put(connid, true);
			}
		}
		
	}

	public synchronized void closeMataDataSource(String connid) throws SQLException, ConnectionFactoryException  {
		SQLManager.getInstance().close(connid);
	}

	private static class FactoryHolder{
        private static final ConnectionFactory instance = new ConnectionFactory();
    }

	public static ConnectionFactory getInstance() {
		return FactoryHolder.instance;
    }

	@Override
	public boolean isShutdown(String connid) throws ConnectionFactoryException {
		if(connectionShutdownInfo.containsKey(connid)) {
			throw new ConnectionFactoryException(VarsqlAppCode.EC_DB_POOL_CLOSE, "db connection shutdown");
		}
		return false;
	}
	
	@Override
	public PoolStatus getStatus(String connid) {
		if(connectionShutdownInfo.containsKey(connid)) {
			return PoolStatus.SHUTDOWN; 
		}
		if(connectionConfig.containsKey(connid)){
			return PoolStatus.START; 
		}
		return PoolStatus.STOP;
	}

	private void init() {
		try {
			Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(Configuration.getInstance().getConnectiondaoPackage())).setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

			Set<Class<?>> types = reflections.getTypesAnnotatedWith(ConnectionInfoConfig.class);
			StringBuffer sb =new StringBuffer();

			for (Class<?> type : types) {
				ConnectionInfoConfig annoInfo = type.getAnnotation(ConnectionInfoConfig.class);
				sb.append("beanType : [").append(annoInfo.beanType());
				sb.append("] beanName : [").append(annoInfo.beanName()).append("]");

				if(BeanType.JAVA.equals(annoInfo.beanType())) {
					connectionInfoDao =	(ConnectionInfoDao)type.getDeclaredConstructor().newInstance(new Object[] {});
				}else {
					connectionInfoDao =	VarsqlSpringBeanUtils.getBean(annoInfo.beanName(), ConnectionInfoDao.class);
				}

				if(annoInfo.primary() == true) {
					break ;
				}
			}

			if(connectionInfoDao ==null) {
				throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_INFO, "ConnectionInfoDao bean null; config info : "+ sb.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new  ConnectionFactoryException("ConnectionInfoDao bean: "+ e.getMessage());
		}
	}

	private ConnectionPoolInterface getPoolBean() {
		return this.connectionPoolType.getPoolBean();
	}
}
