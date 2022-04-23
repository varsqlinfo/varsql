package com.varsql.core.sql.executor.handler;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * -----------------------------------------------------------------------------
* @fileName		: UpdateParameter.java
* @desc		:  update parameter
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateInfo {
	private String sql;

	private Map parameter;
	@Builder
	public UpdateInfo(String sql, Map parameter) {
		this.sql = sql;
		this.parameter = parameter;
	}


}
