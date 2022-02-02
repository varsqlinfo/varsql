package com.varsql.core.connection.beans;

import java.io.Serializable;
import java.util.List;

import com.varsql.core.common.beans.FileInfo;

public class JDBCDriverInfo implements Serializable {
	private static final long serialVersionUID = -3088926375387532055L;
	

	private String driverId;

	private String driverClass;
	

	private List<FileInfo> driverFiles;
	
	@SuppressWarnings("unused")
	private JDBCDriverInfo() {}
	
	public JDBCDriverInfo(String driverId, String driverClass) {
		this.driverId = driverId;
		this.driverClass = driverClass;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public List<FileInfo> getDriverFiles() {
		return this.driverFiles;
	}

	public void setDriverFiles(List<FileInfo> jdbcDriverList) {
		this.driverFiles = jdbcDriverList;
	}
}
