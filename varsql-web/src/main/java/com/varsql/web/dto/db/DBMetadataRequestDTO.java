package com.varsql.web.dto.db;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * Database metadata parameter
* 
* @fileName	: DBMetadataRequestDTO.java
* @author	: ytkim
 */
@Getter
@Setter
public class DBMetadataRequestDTO extends DatabaseParamInfo{
	private String conuid;
	
	public void setConuid(String conuid) {
		this.conuid = conuid; 
		setDatabaseInfo(SecurityUtil.loginInfo().getDatabaseInfo().get(conuid));
	}
}