package com.varsql.core.db.servicemenu;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @FileName  : ObjectTypeTabInfo.java
 * @프로그램 설명 : object type 보여질 텝 정보
 * @Date      : 2019. 2. 21. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public interface ObjectTypeTabInfo {
	
	@JsonFormat(shape= JsonFormat.Shape.OBJECT)
	enum MetadataTab{
		COLUMN("Column","column"), DDL("DDL", "ddl"), INFO("Info", "info");
		
		private String name;
		private String tabid;
		
		MetadataTab(String name , String tabid){
			this.name = name; 
			this.tabid = tabid; 
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getTabid() {
			return this.tabid;
		}
	}
}
