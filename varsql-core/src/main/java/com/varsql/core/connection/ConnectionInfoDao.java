package com.varsql.core.connection;

import com.varsql.core.connection.beans.ConnectionInfo;

public interface ConnectionInfoDao {
	public ConnectionInfo getConnectionInfo(String conuid) throws Exception;
}
