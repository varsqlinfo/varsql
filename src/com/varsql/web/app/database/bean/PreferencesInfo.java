package com.varsql.web.app.database.bean;

import com.varsql.db.vo.DatabaseParamInfo;

/**
 * 
 * @FileName : DatabaseParamInfo.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
public class PreferencesInfo extends DatabaseParamInfo{
	private String prefKey;	
	private String prefVal;
	
	public PreferencesInfo(){
		super();
	}
	
	public String getPrefKey() {
		return prefKey;
	}
	public void setPrefKey(String prefKey) {
		this.prefKey = prefKey;
	}
	public String getPrefVal() {
		return prefVal;
	}
	public void setPrefVal(String prefVal) {
		this.prefVal = prefVal;
	}	
	
	
}
