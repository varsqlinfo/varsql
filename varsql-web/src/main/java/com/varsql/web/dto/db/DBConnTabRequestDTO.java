package com.varsql.web.dto.db;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.web.model.entity.db.DBConnTabEntity;
import com.varsql.web.model.entity.db.DBGroupEntity;
import com.varsql.web.model.entity.sql.SqlFileEntity;

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
@Getter
@Setter
public class DBConnTabRequestDTO extends DatabaseParamInfo{

	private String prevConuid;

	public DBConnTabRequestDTO(){
		super();
	}

	public DBConnTabEntity toEntity() {
		return DBConnTabEntity.builder()
			.vconnid(getVconnid())
			.viewid(getViewid())
			.prevVconnid(getVconnid(prevConuid))
			.viewYn(true)
			.build();
	}
}