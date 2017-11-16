package com.varsql.web.app.database.bean;

import com.varsql.common.util.SecurityUtil;
import com.varsql.db.vo.DatabaseParamInfo;
import com.vartech.common.app.beans.ParamMap;

/**
 * 
 * @FileName : DatabaseParamInfo.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
public class SqlParamInfo extends DatabaseParamInfo{
	
	// sql
	private String sql;
	                                                                                                                                                                             
	// sql_id
	private String sqlId;
	
	// sql_id
	private String sqlTitle;
	
	// limit count
	private String limit;
	
	private String userid;
	
	// sql parameer
	private String sqlParam;
	
	private String exportType;
	
	private String columnInfo;
	
	private ParamMap customInfo;
	
	public SqlParamInfo(){
		super();
		this.userid = SecurityUtil.loginInfo().getUid();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getLimit(int defaultLimit) {
		try{
			return Integer.parseInt(limit);
		}catch(Exception e){}
		
		return defaultLimit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public String getSqlTitle() {
		return sqlTitle;
	}

	public void setSqlTitle(String sqlTitle) {
		this.sqlTitle = sqlTitle;
	}
	
	public ParamMap getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(ParamMap customInfo) {
		this.customInfo = customInfo;
	}
	
	public void addCustomInfo(String key , Object value) {
		if(this.customInfo ==null){
			this.customInfo =  new ParamMap();
		}
		
		this.customInfo.put(key, value);
	}
}
