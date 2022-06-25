package com.varsql.core.db.valueobject;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.varsql.core.auth.User;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName : DatabaseParamInfo.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
@Alias("databaseParamInfo")
public class DatabaseParamInfo{

	private String conuid;

	// base schema info
	private String baseSchema="PUBLIC";

	// schema info
	private String databaseName;
	
	// schema info
	private String schema;

	// base schema
	private boolean baseSchemaFlag;

	// varsql connection id
	private String vconnid;

	// db type
	private String type;

	// db object name
	private String objectName;
	
	// db object names
	private String[] objectNames;

	// objet type
	private String objectType;

	// db vender name
	private String dbType;

	// userid
	private String viewid;
	
	private boolean refresh; 

	private boolean basetableYn;

	private boolean lazyLoad;

	private boolean schemaViewYn;

	private Map<String , Object> custom;

	public String getConuid() {
		return conuid;
	}

	public void setConuid(String conuid) {
		User user = SecurityUtil.loginInfo();
		setConuid(conuid, user.getViewid() , user.getDatabaseInfo().get(conuid));
	}

	public void setConuid(String conuid, String viewid, DatabaseInfo dbInfo) {
		this.viewid =viewid;
		this.conuid = conuid;
		this.vconnid = dbInfo.getVconnid();
		this.type = dbInfo.getType();
		this.dbType = dbInfo.getType();
		this.baseSchema = dbInfo.getSchema();
		this.basetableYn = dbInfo.isBasetableYn();
		this.lazyLoad =dbInfo.isLazyLoad();
		this.schemaViewYn =dbInfo.isSchemaViewYn();
		
		setSchema(this.schema==null ? dbInfo.getSchema() : this.schema);
		setDatabaseName(this.databaseName==null ? dbInfo.getDatabaseName() : this.databaseName);
	}

	protected String getVconnid(String conuid) {
		User user = SecurityUtil.loginInfo();
		if(user.getDatabaseInfo().containsKey(conuid)) {
			return user.getDatabaseInfo().get(conuid).getVconnid();
		}
		return null;
	}

	public String getVconnid() {
		return vconnid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		if(!StringUtils.isBlank(schema)) {
			this.schema = StringUtils.removeSpecialCharacter(schema);
			if(this.basetableYn){
				this.baseSchemaFlag = this.baseSchema.equals(this.schema);
			}else{
				this.baseSchemaFlag = false;
			}
		}
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = StringUtils.isBlank(objectName) ? objectName : StringUtils.removeSpecialCharacter(objectName);
	}
	
	public String[] getObjectNames() {
		return objectNames;
	}

	public void setObjectNames(String objectNames) {
		this.objectNames = objectNames.split(";");
	}

	public String getDbType() {
		return dbType;
	}

	public Map<String , Object> getCustom() {
		return custom;
	}

	public void setCustom(Map custom) {
		this.custom = custom;
	}

	public void addCustom(String key, Object val) {
		if(this.custom ==null){
			this.custom = new HashMap<String ,Object>();
		}

		this.custom.put(key, val);

	}

	public String getViewid() {
		return viewid;
	}

	public String getBaseSchema() {
		return baseSchema;
	}


	public boolean isBaseSchemaFlag() {
		return baseSchemaFlag;
	}

	public boolean isLazyLoad() {
		return lazyLoad;
	}

	public boolean isSchemaViewYn() {
		return schemaViewYn;
	}

	public void setSchemaViewYn(boolean schemaViewYn) {
		this.schemaViewYn = schemaViewYn;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = Boolean.parseBoolean(refresh);
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = StringUtils.isBlank(databaseName) ? databaseName : StringUtils.removeSpecialCharacter(databaseName);
	}
}
