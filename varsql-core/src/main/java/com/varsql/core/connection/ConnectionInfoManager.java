package com.varsql.core.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : ConnectionFactory.java
 * @프로그램 설명 : Connection Factory
 * @Date      : 2018. 2. 13.
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class ConnectionInfoManager{

	private final Logger logger = LoggerFactory.getLogger(ConnectionInfoManager.class);


	private final ConcurrentHashMap<String, ConnectionInfo> connectionConfig = new ConcurrentHashMap<String, ConnectionInfo>();


	private ConnectionInfoDao connectionInfoDao;

	private ConnectionInfoManager() {
		init();
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
	public synchronized ConnectionInfo getConnectionInfo(String connid) throws ConnectionFactoryException{
		if(connectionConfig.containsKey(connid)){
			return connectionConfig.get(connid);
		}else{
			ConnectionInfo connInfo;
			try {
				connInfo = connectionInfoDao.getConnectionInfo(connid);
				
				if(connInfo==null) {
					logger.error("connection info not found : {}", connid);
					throw new ConnectionFactoryException("connection info not found : "+ connid);
				}
				
				DataSource dataSource = JdbcUtils.getDataSource(connInfo);
						
				try(Connection conn = dataSource.getConnection()){
					if(conn == null) {
						throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR ,"createConnectionInfo error : [" + connInfo.getConnid() + "]");
					}
					
					// catalog
					try {
						connInfo.setDatabaseName(conn.getCatalog());
					}catch(SQLException e1) {
						logger.error("connection getCatalog: {}", e1.getMessage());
					}
					
					//schema
					String schema ="";
					try {
						schema = conn.getSchema();
						
						if(StringUtils.isBlank(schema)) {
							try {
								schema = conn.getMetaData().getUserName();
							}catch(SQLException e3) {
								logger.error("connection getUserName: {}", e3.getMessage());
							};
						}
					}catch(SQLException e1) {
						logger.error("connection getSchema: {}", e1.getMessage());
						try {
							schema = conn.getMetaData().getUserName();
						}catch(SQLException e3) {
							logger.error("connection getUserName: {}", e3.getMessage());
						};
					}
					
					connInfo.setSchema(schema);
					
					if(DBVenderType.getDBType(connInfo.getType()).isUseDatabaseName()) {
						connInfo.setSchema(connInfo.getDatabaseName());
					}
				}
				
				this.connectionConfig.put(connid, connInfo);
				
				return connInfo; 
			} catch (Exception e) {
				throw new ConnectionFactoryException(e);
			}
			
		}
	}
	
	/**
	 * connection 정보 존재 하는지 여부 체크. 
	 * 
	 * @param connid
	 * @return
	 */
	public boolean exists(String connid) {
		return this.connectionConfig.containsKey(connid);
	}
	
	/**
	 * item 삭제.
	 * @param connid
	 */
	public void remove(String connid) {
		this.connectionConfig.remove(connid);
	}
	
	public int size() {
		return this.connectionConfig.size();
	}
	
	public Set<Entry<String, ConnectionInfo>> entrySet() {
		return this.connectionConfig.entrySet();
	}

	private static class FactoryHolder{
        private static final ConnectionInfoManager instance = new ConnectionInfoManager();
    }

	public static ConnectionInfoManager getInstance() {
		return FactoryHolder.instance;
    }

	private void init() {
		
		logger.debug("connection info config scan package : {}", Configuration.getInstance().getConnectiondaoPackage());
		try {
			Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(Configuration.getInstance().getConnectiondaoPackage())).setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

			Set<Class<?>> types = reflections.getTypesAnnotatedWith(ConnectionInfoConfig.class);
			StringBuffer sb =new StringBuffer();

			for (Class<?> type : types) {
				ConnectionInfoConfig annoInfo = type.getAnnotation(ConnectionInfoConfig.class);
				sb.append("beanType : [").append(annoInfo.beanType());
				sb.append("] primary : [").append(annoInfo.primary()).append("]");
				sb.append("] beanName : [").append(annoInfo.beanName()).append("]");

				connectionInfoDao =	(ConnectionInfoDao)type.getDeclaredConstructor().newInstance(new Object[] {});
				
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
}
