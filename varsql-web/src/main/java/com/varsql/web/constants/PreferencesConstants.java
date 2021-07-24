package com.varsql.web.constants;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PreferencesConstants.java
* @DESC		: 환결설정 정보  key interface
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2017. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public interface PreferencesConstants {
	enum PREFKEY{
		MAIN_SETTING("main.database.setting")
		,TABLE_EXPORT("tool.table.export")
		,CONTEXTMENU_SERVICEOBJECT("main.contextmenu.serviceobject");
		
		String prefKey; 
		
		PREFKEY(String key){
			this.prefKey = key;
		}
		
		public String key(){
			return prefKey; 
		}
		@Override
		public String toString() {
			return this.prefKey+"";
		}
	}
}
