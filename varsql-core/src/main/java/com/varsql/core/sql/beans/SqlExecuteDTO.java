package com.varsql.core.sql.beans;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.SqlStatementInfo;

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
	
	private String _requid_;
	
	public String get_requid_() {
		return _requid_;
	}

	public void set_requid_(String _requid_) {
		this._requid_ = _requid_;
	}
	
	public void setConuid(String conuid) {
		this.conuid = conuid; 
		setDatabaseInfo(SecurityUtil.loginInfo().getDatabaseInfo().get(conuid));
	}
}
