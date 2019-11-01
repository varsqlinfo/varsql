package com.varsql.app.common.enums;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ViewPage.java
* @DESC		: controller page enum
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 10. 31. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public enum ViewPage {
	DATABASE ("/database")
	, DATABASE_PREFERENCES ("/database/preferences")
	, DATABASE_TOOLS ("/database/tools")
	, LOGIN("/login")
	, JOIN("/join")
	, GUEST("/guest")
	, COMMONPAGE("/commonPage")
	, USER("/user")
	, USER_PREFERENCES("/user/preferences")
	, MANAGER("/manager")
	, ADMIN("/admin");
	
	private String urlPrefix;
	
	ViewPage(String prefix){
		this.urlPrefix = prefix; 
	}
	
	public String getPrefix() {
		return urlPrefix;
	}
	
	public String getPagePath(String path) {
		return String.format("%s%s", urlPrefix, path);
	}
	
}
