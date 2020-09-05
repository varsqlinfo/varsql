package com.varsql.web.dto.sql;

import com.varsql.web.dto.user.RegInfoDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlHistoryResponseDTO.java
* @DESC		: sql user history info
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
public class SqlHistoryResponseDTO{
	private String vconnid; //connid
	private String viewid; //view id
	private String historyId; //history id

	private String startTime; // start time
	private String endTime; // end time
	private int delayTime; // delay time

	private String logSql; // log sql
	private String errorLog; // error log
	private String usrIp; // client ip
	
	private RegInfoDTO regInfo;
	
}
