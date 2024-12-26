package com.varsql.core.db.valueobject;

import java.util.HashMap;
import java.util.Map;

import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.sql.util.SQLUtils;

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
	private String requid$$;
	
	private String ip;
	
	// sql
	private String sql;
	
	// schema info
	private String schema;
		
	// limit count
	private int limit;

	// sql parameer
	private Map sqlParam;
	
	// db object name
	private String objectName;

	// export type (sql, csv, json, xml ...)
	private VarsqlFileType exportType;

	private String columnInfo;
	
	// export charset
	private String charset;
	
	// column alias 
	private boolean useColumnAlias = true;
	
	// format value default = false
	private boolean isFormatValue = true; 
	
	private Map<String , Object> custom;
	
	private DatabaseInfo databaseInfo;

	public SqlStatementInfo(){
		super();
	}

	public String getRequid$$() {
		return this.requid$$;
	}

	public void setRequid$$(String requid$$) {
		this.requid$$ = requid$$;
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

	public Map getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = SQLUtils.stringParamMapToMap(sqlParam);
	}
	
	public void setSqlParamMap(Map sqlParam) {
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

	public boolean isFormatValue() {
		return isFormatValue;
	}

	public void setFormatValue(boolean isFormatValue) {
		this.isFormatValue = isFormatValue;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
}
