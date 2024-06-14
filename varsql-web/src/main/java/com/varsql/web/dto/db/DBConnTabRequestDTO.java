package com.varsql.web.dto.db;

import com.varsql.web.model.entity.db.DBConnTabEntity;
import com.varsql.web.util.DatabaseUtils;
import com.varsql.web.util.SecurityUtil;
import com.vartech.common.utils.StringUtils;

/**
 * db conn tab info
* 
* @fileName	: DBConnTabRequestDTO.java
* @author	: ytkim
 */
public class DBConnTabRequestDTO{
	
	private String conuid;

	private String firstConuid;
	
	private String prevConuid;
	
	private String vconnid;
	
	private String firstVconnid;
	
	private String prevVconnid;
	
	public DBConnTabRequestDTO(){
		super();
	}

	public DBConnTabEntity toEntity() {
		return DBConnTabEntity.builder()
			.vconnid(getVconnid())
			.viewid(SecurityUtil.userViewId())
			.prevVconnid(prevVconnid)
			.viewYn(true)
			.build();
	}

	public String getFirstConuid() {
		return firstConuid;
	}

	public void setFirstConuid(String firstConuid) {
		this.firstConuid = firstConuid;
		
		if(!StringUtils.isBlank(firstConuid)) {
			this.firstVconnid = DatabaseUtils.convertConUidToVconnid(firstConuid);
		}
	}

	public String getPrevConuid() {
		return prevConuid;
	}

	public void setPrevConuid(String prevConuid) {
		this.prevConuid = prevConuid;
		
		if(!StringUtils.isBlank(prevConuid)) {
			this.prevVconnid = DatabaseUtils.convertConUidToVconnid(prevConuid);
		}
	}

	public String getFirstVconnid() {
		return firstVconnid;
	}

	public void setFirstVconnid(String firstVconnid) {
		this.firstVconnid = firstVconnid;
	}

	public String getPrevVconnid() {
		return prevVconnid;
	}

	public String getConuid() {
		return conuid;
	}

	public void setConuid(String conuid) {
		this.conuid = conuid;
		
		if(!StringUtils.isBlank(conuid)) {
			this.vconnid = DatabaseUtils.convertConUidToVconnid(conuid);
		}
	}

	public String getVconnid() {
		return vconnid;
	}
}