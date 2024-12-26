package com.varsql.core.db.valueobject;

import com.varsql.core.connection.beans.ConnectionInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Database Info
* 
* @fileName	: DatabaseInfo.java
* @author	: ytkim
 */
@Getter
@Setter
public class DatabaseInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String vconnid;
	private String type;
	private String name;
	private boolean basetableYn;
	private boolean lazyLoad;
	private boolean schemaViewYn;
	private String version;
	private int maxSelectCount;
	
	public static DatabaseInfo toDatabaseInfo(ConnectionInfo connectionInfo) {
		return DatabaseInfo.builder()
				.vconnid(connectionInfo.getConnid())
				.type(connectionInfo.getType())
				.name(connectionInfo.getAliasName())
				.maxSelectCount(connectionInfo.getExportCount())
				.build();
	}

	@Builder
	public DatabaseInfo(String vconnid, String type, String name, String basetableYn, String lazyLoad, String version, 
			String schemaViewYn, int maxSelectCount){
		
		this.vconnid= vconnid;
		this.type= type.toUpperCase();
		this.name= name;
		this.basetableYn= "Y".equals(basetableYn);
		this.lazyLoad= "Y".equals(lazyLoad);
		this.schemaViewYn= "Y".equals(schemaViewYn);
		this.version= version;
		this.maxSelectCount = maxSelectCount;
	}
}
