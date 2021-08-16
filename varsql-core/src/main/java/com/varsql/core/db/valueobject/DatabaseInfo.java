package com.varsql.core.db.valueobject;

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
	private boolean isUseColumnLabel;
	private long version;
	private int maxSelectCount;

	public DatabaseInfo(String vconnid,String connUUID, String type,String name,String schema,String basetableYn,String lazyLoad,long version, String schemaViewYn, int maxSelectCount, String useColumnLabel){
		this.vconnid= vconnid;
		this.connUUID= connUUID;
		this.type= type.toUpperCase();
		this.name= name;
		this.schema= schema;
		this.basetableYn= "Y".equals(basetableYn);
		this.lazyLoad= "Y".equals(lazyLoad);
		this.schemaViewYn= "Y".equals(schemaViewYn);
		this.version= version;
		this.maxSelectCount = maxSelectCount;
		this.isUseColumnLabel = "Y".equals(useColumnLabel);
	}

	public String getVconnid() {
		return vconnid;
	}

	public void setVconnid(String vconnid) {
		this.vconnid = vconnid;
	}

	public String getConnUUID() {
		return connUUID;
	}

	public void setConnUUID(String connUUID) {
		this.connUUID = connUUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public boolean isBasetableYn() {
		return basetableYn;
	}

	public void setBasetableYn(boolean basetableYn) {
		this.basetableYn = basetableYn;
	}

	public boolean isLazyLoad() {
		return lazyLoad;
	}

	public void setLazyLoad(boolean lazyLoad) {
		this.lazyLoad = lazyLoad;
	}

	public boolean isSchemaViewYn() {
		return schemaViewYn;
	}

	public void setSchemaViewYn(boolean schemaViewYn) {
		this.schemaViewYn = schemaViewYn;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getMaxSelectCount() {
		return maxSelectCount;
	}

	public void setMaxSelectCount(int maxSelectCount) {
		this.maxSelectCount = maxSelectCount;
	}

	public boolean isUseColumnLabel() {
		return isUseColumnLabel;
	}
}
