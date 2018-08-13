package com.varsql.app.database.beans;

import java.sql.Timestamp;

import com.varsql.app.util.VarsqlUtil;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlUserHistoryInfo.java
* @DESC		: sql user history info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 3. 2. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlUserHistoryInfo{
	private String vconnid; //connid
	private String viewid; //view id
	private String historyId; //history id

	private String startTime; // start time
	private String endTime; // end time
	private int delayTime; // delay time

	private String logSql; // log sql
	private String errorLog; // error log
	private String usrIp; // client ip
	
	public String getVconnid() {
		return vconnid;
	}
	public void setVconnid(String vconnid) {
		this.vconnid = vconnid;
	}
	public String getViewid() {
		return viewid;
	}
	public void setViewid(String viewid) {
		this.viewid = viewid;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	public String getLogSql() {
		return logSql;
	}
	public void setLogSql(String logSql) {
		this.logSql = logSql;
	}
	public String getErrorLog() {
		return errorLog;
	}
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	public String getUsrIp() {
		return usrIp;
	}
	public void setUsrIp(String usrIp) {
		this.usrIp = usrIp;
	}
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
}
