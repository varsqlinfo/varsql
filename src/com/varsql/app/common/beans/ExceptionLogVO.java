package com.varsql.app.common.beans;

import org.apache.commons.lang3.StringUtils;

import com.varsql.core.common.util.CommUtil;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.vartech.common.utils.VartechUtils;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: CommonExceptionVO.java
* @DESC		: 예외 bean 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 15. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class ExceptionLogVO{

	// id
	private String exceotionId;
	
	// excepton type
	private String exceptionType;

	// 메시지
	private String exceptionTitle;
	
	private String serverId;

	private String exceptionCont;

	private String regId;
	
	public ExceptionLogVO(String exceptionType, Throwable e) {
		this.exceptionType= exceptionType;
		this.exceptionCont = CommUtil.getExceptionStr(e);
		this.exceptionCont = this.exceptionCont.substring(0 , 2000);
		
		this.exceptionTitle = e.getMessage();
		
		if(this.exceptionTitle.length() > 1500) {
			this.exceptionTitle = this.exceptionTitle.substring(0 , 1500);
		}
		
		this.exceotionId = UUIDUtil.generateUUID();
		this.serverId = CommUtil.getHostname();
		this.regId = SecurityUtil.loginId();
	}

	public String getExceotionId(){
		return this.exceotionId;
	}
	
	public String getServerId(){
		return this.serverId;
	}
	
	public String getRegId(){
		return this.regId;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public String getExceptionCont() {
		return exceptionCont;
	}

	public String getExceptionTitle() {
		return exceptionTitle;
	}

}