package com.varsql.core.db.valueobject;

import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.SqlDataConstants;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlStatementInfo.java
* @DESC		: sql statement info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2020. 12. 11. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlStatementInfo extends DatabaseParamInfo{

	// sql
	private String sql;

	// limit count
	private int limit;

	// sql parameer
	private String sqlParam;

	// export type (sql, csv, json, xml ...)
	private VarsqlFileType exportType;

	private String columnInfo;
	
	private String charset;
	
	private boolean useColumnAlias = true;

	public SqlStatementInfo(){
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

	public VarsqlFileType getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = VarsqlFileType.getFileType(exportType);
	}

	public String getColumnInfo() {
		return columnInfo;
	}

	public void setColumnInfo(String columnInfo) {
		this.columnInfo = columnInfo;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {

		return new StringBuilder()
				.append("sql : ").append(sql)
				.append(" , sqlParam : ").append(sqlParam)
				.append(" , columnInfo : ").append(columnInfo)
				.append(" , charset : ").append(charset)
				.toString();
	}

	public boolean isUseColumnAlias() {
		return useColumnAlias;
	}

	public void setUseColumnAlias(boolean useColumnAlias) {
		this.useColumnAlias = useColumnAlias;
	}

	
}
