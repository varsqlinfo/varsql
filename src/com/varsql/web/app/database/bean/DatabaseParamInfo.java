package com.varsql.web.app.database.bean;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.varsql.common.util.SecurityUtil;
import com.varsql.db.vo.DatabaseInfo;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.util.VarsqlUtil;

/**
 * 
 * @FileName : DatabaseParamInfo.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
public class DatabaseParamInfo{
	
	@NotEmpty
	@Size(min=6, max=6)
	private String conuid;
	
	// schema info
	private String schema;
	
	// varsql connection id
	private String vconnid;
	
	// db type
	private String type;
	
	// db object name
	private String objectName;
	
	// objet type gubun
	private String gubun;
	
	public String getConuid() {
		return conuid;
	}

	public void setConuid(String conuid) {
		this.conuid = conuid;
		this.vconnid = SecurityUtil.loginInfo().getDatabaseInfo().get(conuid).getVconnid();
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


	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
}
