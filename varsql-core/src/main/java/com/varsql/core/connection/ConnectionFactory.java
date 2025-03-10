package com.varsql.core.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
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

	private final ConcurrentHashMap<String, Boolean> connectionShutdownInfo = new ConcurrentHashMap<String, Boolean>();

	private ConnectionFactory() {}

	@Override
	public Connection getConnection(String connid) throws ConnectionFactoryException  {
		ConnectionInfo connInfo  = createPool(connid);

		if(connInfo != null){
			if(isShutdown(connid)) {
				throw new ConnectionFactoryException(VarsqlAppCode.EC_DB_POOL_CLOSE, "db connection refused");
			}
			
			return connectionPoolType.getPoolBean().getConnection(connInfo);
		}

		throw new ConnectionFactoryException(" null connection infomation : "+ connid);
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
	private synchronized ConnectionInfo createPool(String connid) throws ConnectionFactoryException{
		try {
			
			ConnectionInfo connInfo = ConnectionInfoManager.getInstance().getConnectionInfo(connid);
			
			if(connInfo.isCreateConnectionPool()) {
				return connInfo; 
			}
			
			getPoolBean().createDataSource(connInfo);
			
			try(Connection conn =  connectionPoolType.getPoolBean().getConnection(connInfo);){
				if(conn == null) {
					throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR ,"createConnectionInfo error : [" + connInfo.getConnid() + "]");
				}
			}
			
			connInfo.setCreateConnectionPool(true);
			
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

		logger.info("db pool shutdown : {}" , connid);

		if(ConnectionInfoManager.getInstance().exists(connid)){
			ConnectionFactory.getInstance().getPoolBean().poolShutdown( ConnectionInfoManager.getInstance().getConnectionInfo(connid));
		}
		closeMataDataSource(connid);
		connectionShutdownInfo.put(connid, true);
	}
	
	public static void poolShutdown() throws SQLException, ConnectionFactoryException  {
		getInstance().allPoolShutdown();
	}
	
	/**
	 * 모든 커넥션 풀 닫기
	 * @throws SQLException
	 * @throws ConnectionFactoryException
	 */
	private synchronized void allPoolShutdown() throws SQLException, ConnectionFactoryException  {
		
		if(ConnectionInfoManager.getInstance().size() > 0) {
			for(Entry<String, ConnectionInfo> connEntry: ConnectionInfoManager.getInstance().entrySet()) {
				String connid = connEntry.getKey();
				poolShutdown(connid);
			}
		}
	}

	public synchronized void closeMataDataSource(String connid) throws SQLException, ConnectionFactoryException  {
		SQLManager.getInstance().close(connid);
		ConnectionInfoManager.getInstance().remove(connid);
	}

	private static class FactoryHolder{
        private static final ConnectionFactory instance = new ConnectionFactory();
    }

	public static ConnectionFactory getInstance() {
		return FactoryHolder.instance;
    }
	
	/**
	 * shutdown check
	 */
	@Override
	public boolean isShutdown(String connid) throws ConnectionFactoryException {
		if(connectionShutdownInfo.containsKey(connid)) {
			return true; 
		}
		return false;
	}
	
	/**
	 * pool status check
	 */
	@Override
	public PoolStatus getStatus(String connid) {
		if(connectionShutdownInfo.containsKey(connid)) {
			return PoolStatus.SHUTDOWN; 
		}
		if(ConnectionInfoManager.getInstance().exists(connid)){
			return ConnectionInfoManager.getInstance().getConnectionInfo(connid).isCreateConnectionPool()? PoolStatus.START : PoolStatus.STOP; 
		}
		return PoolStatus.STOP;
	}

	public ConnectionPoolInterface getPoolBean() {
		return this.connectionPoolType.getPoolBean();
	}
}
