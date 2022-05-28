package com.varsql.web.dto.db;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.model.entity.db.DBConnTabEntity;
import com.vartech.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBConnTabRequestDTO.java
* @desc		: db conn tab info
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 7. 4. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class DBConnTabRequestDTO extends DatabaseParamInfo{

	private String firstConuid;
	
	private String prevConuid;
	
	private String firstVconnid;
	
	private String prevVconnid;

	public DBConnTabRequestDTO(){
		super();
	}

	public DBConnTabEntity toEntity() {
		return DBConnTabEntity.builder()
			.vconnid(getVconnid())
			.viewid(getViewid())
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
			this.firstVconnid = getVconnid(firstConuid);
		}
	}

	public String getPrevConuid() {
		return prevConuid;
	}

	public void setPrevConuid(String prevConuid) {
		this.prevConuid = prevConuid;
		
		if(!StringUtils.isBlank(prevConuid)) {
			this.prevVconnid = getVconnid(prevConuid);
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
}