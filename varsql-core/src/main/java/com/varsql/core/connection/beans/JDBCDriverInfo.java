package com.varsql.core.connection.beans;

import java.io.Serializable;
import java.util.List;

import com.vartech.common.app.beans.FileInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JDBCDriverInfo implements Serializable {
	private static final long serialVersionUID = -3088926375387532055L;
	

	private String providerId;
	
	private String driverId;

	private String driverClass;
	
	private List<FileInfo> driverFiles;
	
	@SuppressWarnings("unused")
	private JDBCDriverInfo() {}
	
	@Builder
	public JDBCDriverInfo(String providerId, String driverId, String driverClass, List<FileInfo> driverFiles) {
		this.providerId = providerId;
		this.driverId = driverId;
		this.driverClass = driverClass;
		this.driverFiles = driverFiles;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("driverClass : ").append(driverClass)
				.append(", driverFiles : ").append(driverFiles)
				.toString();
	}
}
