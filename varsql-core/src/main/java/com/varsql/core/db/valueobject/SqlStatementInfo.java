package com.varsql.core.db.valueobject;

import java.util.HashMap;
import java.util.Map;

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
public class SqlStatementInfo{

	// sql
	private String sql;
	
	// schema info
	private String schema;
		
	// limit count
	private int limit;

	// sql parameer
	private String sqlParam;
	
	// db object name
	private String objectName;

	// export type (sql, csv, json, xml ...)
	private VarsqlFileType exportType;

	private String columnInfo;
	
	private String charset;
	
	private boolean useColumnAlias = true;
	
	private Map<String , Object> custom;
	
	private DatabaseInfo databaseInfo;

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
		this.limit =limit;
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

	public boolean isUseColumnAlias() {
		return useColumnAlias;
	}

	public void setUseColumnAlias(boolean useColumnAlias) {
		this.useColumnAlias = useColumnAlias;
	}

	public Map<String , Object> getCustom() {
		return custom;
	}

	public void setCustom(Map custom) {
		this.custom = custom;
	}

	public void addCustom(String key, Object val) {
		if(this.custom ==null){
			this.custom = new HashMap<String ,Object>();
		}

		this.custom.put(key, val);

	}

	public DatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(DatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
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

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
}
