package com.varsql.web.dto.sql;

import com.varsql.core.db.valueobject.SqlStatementInfo;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlFileRequestDTO.java
* @DESC		: sql file info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlExecuteDTO extends SqlStatementInfo{
	private String _requid_;
	
	public String get_requid_() {
		return _requid_;
	}

	public void set_requid_(String _requid_) {
		this._requid_ = _requid_;
	}
}
