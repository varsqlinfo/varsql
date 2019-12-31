package com.varsql.core.connection.pool;

import java.sql.Connection;

import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.exception.ConnectionFactoryException;

/**
 * 
 * @FileName  : ConnectionPoolInterface.java
 * @프로그램 설명 : connection pool 설정 interface
 * @Date      : 2018. 2. 8. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public interface ConnectionPoolInterface {
	public void createDataSource(ConnectionInfo connInfo)throws ConnectionFactoryException;
	public Connection getConnection(ConnectionInfo connInfo)throws ConnectionFactoryException;
	public void poolShutdown(ConnectionInfo connInfo) throws ConnectionFactoryException;
}
