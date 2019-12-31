package com.varsql.core.db.beans;

/**
 * 
 * @FileName  : DatabaseInfo.java
 * @프로그램 설명 : database info
 * @Date      : 2019. 4. 19. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DatabaseInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String vconnid;
	private String connUUID;
	private String type;
	private String name;
	private String schema;
	private boolean basetableYn;
	private boolean lazyLoad;
	private boolean schemaViewYn;
	private String version;
	private int maxSelectCount;
	
	public DatabaseInfo(String vconnid,String connUUID, String type,String name,String schema,String basetableYn,String lazyLoad,String version, String schemaViewYn, int maxSelectCount){
		this.vconnid= vconnid;
		this.connUUID= connUUID;
		this.type= type.toUpperCase();
		this.name= name;
		this.schema= schema;
		this.basetableYn= ("Y".equals(basetableYn) ? true :false);
		this.lazyLoad= ("Y".equals(lazyLoad) ? true :false);
		this.schemaViewYn= ("Y".equals(schemaViewYn) ? true :false);
		this.version= version;
		this.maxSelectCount = maxSelectCount; 
	}
	
	public String getVconnid() {
		return vconnid;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getConnUUID() {
		return connUUID;
	}
	
	public boolean isBasetableYn() {
		return basetableYn;
	}
	
	public boolean isLazyLoad() {
		return lazyLoad;
	}
	public boolean isSchemaViewYn() {
		return schemaViewYn;
	}

	/**
	 * @return the maxSelectCount
	 */
	public int getMaxSelectCount() {
		return maxSelectCount;
	}
}
