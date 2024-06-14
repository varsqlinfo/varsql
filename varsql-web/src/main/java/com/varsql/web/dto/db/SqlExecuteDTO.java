package com.varsql.web.dto.db;

import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.web.util.SecurityUtil;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlExecuteDTO.java
* @DESC		: Sql execute dto
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlExecuteDTO extends SqlStatementInfo{
	
	private String conuid;
	
	public void setConuid(String conuid) {
		this.conuid = conuid; 
		setDatabaseInfo(SecurityUtil.loginInfo().getDatabaseInfo().get(conuid));
	}
}
