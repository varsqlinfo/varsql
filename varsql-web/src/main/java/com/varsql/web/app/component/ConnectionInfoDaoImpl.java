package com.varsql.web.app.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.connection.ConnectionInfoConfig;
import com.varsql.core.connection.ConnectionInfoDao;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.web.util.VarsqlBeanUtils;

@ConnectionInfoConfig(primary = true)
public class ConnectionInfoDaoImpl implements ConnectionInfoDao {

	private final Logger logger = LoggerFactory.getLogger(ConnectionInfoDaoImpl.class);
	
	ConnectionInfoComponent connectionInfoDao = null; 
	
	public ConnectionInfoDaoImpl() {
		connectionInfoDao =VarsqlBeanUtils.getBean("connectionInfoDao", ConnectionInfoComponent.class);
	}

	@Override
	public ConnectionInfo getConnectionInfo(String connid) throws Exception {
		logger.debug("connection info connid : {}", connid);
		return connectionInfoDao.getConnectionInfo(connid);
	}
}
