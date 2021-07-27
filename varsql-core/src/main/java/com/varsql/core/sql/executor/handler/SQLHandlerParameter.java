package com.varsql.core.sql.executor.handler;

import java.util.List;
import java.util.Map;

import com.varsql.core.sql.beans.GridColumnInfo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * -----------------------------------------------------------------------------
* @fileName		: SQLHandlerParameter.java
* @desc		:  sql handler parameter
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
public class SQLHandlerParameter {
	private String sql;

	private Map parameter;

	private Object rowObject;

	private List<GridColumnInfo> columnInfoList;

	@Builder
	public SQLHandlerParameter(String sql, Map parameter, Object rowObject, List<GridColumnInfo> columnInfoList) {
		this.sql = sql;
		this.parameter = parameter;
		this.rowObject = rowObject;
		this.columnInfoList = columnInfoList;
	}


}
