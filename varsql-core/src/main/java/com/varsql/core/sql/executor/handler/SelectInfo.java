package com.varsql.core.sql.executor.handler;

import java.util.List;

import com.varsql.core.sql.beans.GridColumnInfo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * -----------------------------------------------------------------------------
* @fileName		: SelectParameter.java
* @desc		:  select parameter
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
public class SelectInfo {
	private Object rowObject;

	private List<GridColumnInfo> columnInfoList;

	@Builder
	public SelectInfo(Object rowObject, List<GridColumnInfo> columnInfoList) {
		this.rowObject = rowObject;
		this.columnInfoList = columnInfoList;
	}


}
