package com.varsql.core.db.valueobject;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.varsql.core.connection.ConnectionInfoManager;
import com.vartech.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @FileName : DatabaseParamInfo.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
@Getter
@Setter
@Alias("databaseParamInfo")
public class DatabaseParamInfo{

	// varsql connection id
	private String vconnid;

	// base schema info
	private String baseSchema="PUBLIC";

	// schema info
	private String databaseName;
	
	// schema info
	private String schema;

	// base schema
	private boolean baseSchemaFlag;

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

	private boolean refresh; 

	private boolean basetableYn;

	private boolean lazyLoad;

	private boolean schemaViewYn;

	private Map<String , Object> custom;
	
	public DatabaseParamInfo(){}
	
	public DatabaseParamInfo(DatabaseInfo databaseInfo){
		setDatabaseInfo(databaseInfo);
	}
	
	public void setDatabaseInfo(DatabaseInfo databaseInfo) {
		this.vconnid = databaseInfo.getVconnid();
		this.type = databaseInfo.getType();
		this.dbType = databaseInfo.getType();
		this.baseSchema = ConnectionInfoManager.getInstance().getConnectionInfo(this.vconnid).getSchema() ;
		this.schema = this.baseSchema;
		this.basetableYn = databaseInfo.isBasetableYn();
		this.lazyLoad =databaseInfo.isLazyLoad();
		this.schemaViewYn =databaseInfo.isSchemaViewYn();
		
		setDatabaseName(StringUtils.isBlank(this.databaseName) ? ConnectionInfoManager.getInstance().getConnectionInfo(this.vconnid).getDatabaseName() : this.databaseName);
	}

	public void addCustom(String key, Object val) {
		if(this.custom ==null){
			this.custom = new HashMap<String ,Object>();
		}

		this.custom.put(key, val);
	}
	
	public void setRefresh(String refresh) {
		this.refresh = Boolean.parseBoolean(refresh);
	}
	
	public void setSchema(String schema) {
		this.schema = StringUtils.isBlank(schema) ?  this.baseSchema  : schema;
	}
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = StringUtils.isBlank(databaseName) ? "" : StringUtils.removeSpecialCharacter(databaseName);
	}
	
}
