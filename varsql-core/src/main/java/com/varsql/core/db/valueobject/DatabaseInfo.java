package com.varsql.core.db.valueobject;

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
	private String databaseName;
	private String schema;
	private boolean basetableYn;
	private boolean lazyLoad;
	private boolean schemaViewYn;
	private boolean isUseColumnLabel;
	private long version;
	private int maxSelectCount;

	@Builder
	public DatabaseInfo(String vconnid, String type, String name, 
			String schema, String basetableYn, String lazyLoad, long version, 
			String schemaViewYn, int maxSelectCount, String useColumnLabel, String databaseName){
		
		this.vconnid= vconnid;
		this.type= type.toUpperCase();
		this.name= name;
		this.schema= schema;
		this.basetableYn= "Y".equals(basetableYn);
		this.lazyLoad= "Y".equals(lazyLoad);
		this.schemaViewYn= "Y".equals(schemaViewYn);
		this.version= version;
		this.maxSelectCount = maxSelectCount;
		this.isUseColumnLabel = "Y".equals(useColumnLabel);
		this.databaseName = databaseName;
	}
}
