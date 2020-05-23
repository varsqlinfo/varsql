package com.varsql.web.dto.sql;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.constants.SqlDataConstants;

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
public class SqlExecuteDTO extends DatabaseParamInfo{

	// sql
	private String sql;

	// limit count
	private int limit;

	// sql parameer
	private String sqlParam;

	private String exportType;

	private String columnInfo;

	public SqlExecuteDTO(){
		super();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if(limit > -1) {
			this.limit =limit;
		}else {
			this.limit = SqlDataConstants.DEFAULT_LIMIT_ROW_COUNT;
		}

	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getColumnInfo() {
		return columnInfo;
	}

	public void setColumnInfo(String columnInfo) {
		this.columnInfo = columnInfo;
	}
}
