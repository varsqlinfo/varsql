package com.varsql.web.dto.db;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DBConnTabResponseDTO.java
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
@NoArgsConstructor
@ToString
public class DBConnTabResponseDTO{

	private String name;

	private String conuid;

	private String prevConuid;

	private boolean viewYn;

	@Builder
	public DBConnTabResponseDTO(String name, String conuid, String prevConuid, boolean viewYn) {
		this.name = name;
		this.conuid = conuid;
		this.prevConuid = prevConuid;
		this.viewYn = viewYn;
	}
}