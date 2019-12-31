package com.varsql.core.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.pool.ConnectionPoolInterface;
import com.varsql.core.connection.pool.PoolType;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.sql.util.SQLUtil;

/**
 * 
 * @FileName  : ConnectionFactory.java
 * @프로그램 설명 : Connection Factory 
 * @Date      : 2018. 2. 13. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class ConnectionFactory implements ConnectionContext{
	
	private final static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private PoolType connectionPoolType = PoolType.DBCP2;
	
	private ConcurrentHashMap<String, ConnectionInfo> connectionConfig = new ConcurrentHashMap<String, ConnectionInfo>();
	
	private ConnectionFactory(){}
	
	@Override
	public Connection getConnection() throws ConnectionFactoryException {
		return getConnection(DEFAULT_CONN_ID);
	}
	
	@Override
	public Connection getConnection(String connid) throws ConnectionFactoryException  {
		return getConnection(connid ,true);
	}
	
	public Connection getConnection(String connid , boolean returnFlag) throws ConnectionFactoryException  {
		ConnectionInfo connInfo  = getConnectionInfo(connid);
		
		if(connInfo != null){
			return connectionPoolType.getPoolBean().getConnection(connInfo);
		}
		
		throw new ConnectionFactoryException(" null connection infomation : "+ connid);
	}
	
	public void createPool(ConnectionInfo connInfo) throws SQLException, ConnectionFactoryException  {
		connectionPoolType.getPoolBean().createDataSource(connInfo);
		connectionConfig.put(connInfo.getConnid(), connInfo);
	}
	
	public ConnectionPoolInterface getPoolBean(){
		return connectionPoolType.getPoolBean();
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
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			
			conn = getConnection();
			pstmt  = conn.prepareStatement("select a.*, b.URL_FORMAT, b.DBDRIVER,b.VALIDATION_QUERY from VTCONNECTION a left outer join  VTDBTYPE_DRIVER b on a.VDRIVER = b.DRIVER_ID	where VCONNID= ?");
			
			pstmt.setString(1, connid);
			
			rs= pstmt.executeQuery();
			
			if(rs==null){
				throw new ConnectionFactoryException("not valid connection infomation :"+ connid);
			}
			
			rs.next();
			
			ConnectionInfo connInfo = new ConnectionInfo();
		
			connInfo.setConnid(rs.getString(VarsqlConstants.CONN_ID));
			connInfo.setAliasName(rs.getString(VarsqlConstants.CONN_NAME));
			connInfo.setType(rs.getString(VarsqlConstants.CONN_TYPE).toLowerCase());
			connInfo.setDriver(rs.getString(VarsqlConstants.DBDRIVER));
			
			String urlDirectYn=rs.getString(VarsqlConstants.CONN_URL_DIRECT_YN);
			
			if("Y".equals(urlDirectYn)){
				connInfo.setUrl(rs.getString(VarsqlConstants.CONN_URL));
			}else {
				// jdbc url 생성.
				connInfo.setUrl(VarsqlJdbcUtil.getJdbcUrl(rs.getString(VarsqlConstants.CONN_URL_FORMAT)
						, rs.getString(VarsqlConstants.CONN_SERVERIP)
						, rs.getString(VarsqlConstants.CONN_PORT)
						, rs.getString(VarsqlConstants.CONN_DATABASENAME)
				));
			}
			
			connInfo.setUsername(rs.getString(VarsqlConstants.CONN_VID));
			connInfo.setPassword(rs.getString(VarsqlConstants.CONN_PW));
			connInfo.setPool_opt(rs.getString(VarsqlConstants.CONN_POOLOPT));
			connInfo.setConnection_opt(rs.getString(VarsqlConstants.CONN_CONNOPT));
			connInfo.setMax_active(NumberUtils.toInt(rs.getString(VarsqlConstants.CONN_MAX_ACTIVE), 10));
			connInfo.setMin_idle(NumberUtils.toInt(rs.getString(VarsqlConstants.CONN_MIN_IDLE), 3));
			connInfo.setConnectionTimeOut(NumberUtils.toInt(rs.getString(VarsqlConstants.CONN_TIMEOUT), 18000));
			connInfo.setExportCount(NumberUtils.toInt(rs.getString(VarsqlConstants.CONN_EXPORTCOUNT), 1000));
			
			String conn_query = rs.getString(VarsqlConstants.CONN_QUERY);
			String dbvalidation_query = rs.getString(VarsqlConstants.VALIDATION_QUERY);
			
			conn_query = conn_query ==null?"":conn_query;
			dbvalidation_query = dbvalidation_query ==null?"":dbvalidation_query;
			
			String validation_query = !"".equals(conn_query.trim()) ? conn_query: 
				( !"".equals(dbvalidation_query.trim()) ?  dbvalidation_query : ValidationProperty.getInstance().validationQuery(connInfo.getType()));
			
			
			logger.debug("valication_query : {}",validation_query);
			
			connInfo.setValidation_query(validation_query);
			
			
			try{
				Class.forName(connInfo.getDriver());
			}catch(Exception e){
				logger.error("getConnection driver class  {} " ,connInfo.getDriver());
				logger.error("connInfo {} " ,connInfo);
				logger.error("getConnection :",e);
				throw new ConnectionFactoryException(" driver class not found : "+ connInfo.getDriver());
			}
			
			ConnectionFactory.getInstance().getPoolBean().createDataSource(connInfo);
			
			connectionConfig.put(connid, connInfo);
			
			SQLManager.getInstance().setSQLMapper(connInfo, this);
			
			return connInfo; 
		}catch (Exception e) {
			logger.error("empty connection info" , e);
			throw new ConnectionFactoryException("empty connection info : [" +connid+"]" , e);
		}finally{
			SQLUtil.close(conn , pstmt, rs);
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
		createConnectionInfo(connid);
	}
	
	private static class FactoryHolder{
        private static final ConnectionFactory instance = new ConnectionFactory();
    }
	
	public static ConnectionFactory getInstance() {
		return FactoryHolder.instance;
    }
}
