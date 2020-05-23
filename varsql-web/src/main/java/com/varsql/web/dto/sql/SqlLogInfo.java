package com.varsql.web.dto.sql;

import com.varsql.web.util.VarsqlUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
public class SqlLogInfo{
	private String vconnid; //connid

	private String viewid; //view id

	private long startTime; // start time millis

	private int sMm; //	start month

	private int sDd; // start day

	private int sHh; // start hour

	private long endTime; // end millis

	private int delayTime; // delay time

	private double resultCount; //result count

	private String commandType; // execute query command
	
	private String usrIp; // client ip

	public int getDelayTime(){
		return (int)((this.endTime- this.startTime)/1000);
	}
}
