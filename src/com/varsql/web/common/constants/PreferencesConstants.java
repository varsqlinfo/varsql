package com.varsql.web.common.constants;

public interface PreferencesConstants {
	enum PREFKEY{
		TABLE_EXPORT("tool.table.export");
		
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
