package com.varsql.app.database.beans;

import java.sql.Timestamp;

import com.varsql.app.util.VarsqlUtil;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlLogInfo.java
* @DESC		: sql log info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 3. 2. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlLogInfo{
	private String vconnid; //connid

	private String viewid; //view id

	private String startTime; // start time
	private long startTimeMillis; // start time millis

	private int sMm; //	start month

	private int sDd; // start day

	private int sHh; // start hour

	private String endTime; // end time
	private long endTimeMillis; // end millis

	private int delayTime; // delay time

	private String logSql; // log sql

	private double resultCount; //result count

	private String commandType; // execute query command
	
	private String usrIp; // client ip

	public String getVconnid(){
		return this.vconnid;
	}
	public void setVconnid(String vconnid){
		this.vconnid=vconnid;
	}
	public String getViewid(){
		return this.viewid;
	}
	public void setViewid(String viewid){
		this.viewid=viewid;
	}
	public String getStartTime(){
		return this.startTime;
	}
	public void setStartTime(long startTime){
		
		this.startTimeMillis  = startTime;
		this.startTime=VarsqlUtil.getCurrentTimestamp(this.startTimeMillis);
		this.setDelayTime();
	}
	public int getSMm(){
		return this.sMm;
	}
	public void setSMm(int sMm){
		this.sMm=sMm;
	}
	public int getSDd(){
		return this.sDd;
	}
	public void setSDd(int sDd){
		this.sDd=sDd;
	}
	public int getSHh(){
		return this.sHh;
	}
	public void setSHh(int sHh){
		this.sHh=sHh;
	}
	public String getEndTime(){
		return this.endTime;
	}
	public void setEndTime(long endTime){
		this.endTimeMillis = endTime;
		this.endTime=VarsqlUtil.getCurrentTimestamp(this.endTimeMillis);
		this.setDelayTime();
	}
	public int getDelayTime(){
		return this.delayTime;
	}
	public void setDelayTime(){
		this.delayTime=(int) ((this.endTimeMillis- this.startTimeMillis)/1000);
	}
	public String getLogSql(){
		return this.logSql;
	}
	public void setLogSql(String logSql){
		this.logSql=logSql;
	}
	public double getResultCount(){
		return this.resultCount;
	}
	public void setResultCount(double resultCount){
		this.resultCount=resultCount;
	}
	public String getCommandType(){
		return this.commandType;
	}
	public void setCommandType(String commandType){
		this.commandType=commandType;
	}
	public String getUsrIp() {
		return usrIp;
	}
	public void setUsrIp(String usrIp) {
		this.usrIp = usrIp;
	}
}
