package com.varsql.web.dto.db;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.varsql.web.model.entity.db.DBGroupEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DbGroupRequestDTO.java
* @desc		: db group
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2019. 8. 9. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
public class DBGroupRequestDTO{
	private String groupId;

	@NotEmpty
	@Size(max=255)
	private String groupName;

	@Size(max=2000)
	private String groupDesc;

	public DBGroupEntity toEntity() {
		return DBGroupEntity.builder()
				.groupName(groupName)
				.groupDesc(groupDesc)
				.groupId(groupId).build();
	}

}