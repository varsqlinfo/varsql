package com.varsql.core.db.beans.ddl;

import com.varsql.core.db.beans.BaseObjectInfo;

/**
 * 
 * @FileName  : DDLInfo.java
 * @프로그램 설명 : ddl info
 * @Date      : 2019. 1. 2. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DDLInfo extends BaseObjectInfo{
	// createScript
	private String createScript;

	public String getCreateScript() {
		return createScript;
	}

	public void setCreateScript(String createScript) {
		this.createScript = createScript;
	}

}

