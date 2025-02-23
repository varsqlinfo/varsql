package com.varsql.core.db.valueobject;

import java.util.ArrayList;
import java.util.List;

import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.servicemenu.ObjectTypeTabInfo;

/**
 * 
 * @FileName  : ServiceObject.java
 * @프로그램 설명 : db service object bean
 * @Date      : 2019. 2. 21. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ServiceObject {
	private String name; 
	private String contentid;
	private List<ObjectTypeTabInfo.MetadataTab> tabList = new ArrayList<ObjectTypeTabInfo.MetadataTab>();
	
	public ServiceObject(ObjectType objectType, ObjectTypeTabInfo.MetadataTab... metabTab){
		this(objectType, true);
	}
	
	public ServiceObject(ObjectType objectType, boolean baseMenuFlag, ObjectTypeTabInfo.MetadataTab... metabTab){
		this.name = objectType.getObjectTypeName();
		this.contentid = objectType.getObjectTypeId();
		
		if(baseMenuFlag){
			tabList.add(ObjectTypeTabInfo.MetadataTab.COLUMN);
			tabList.add(ObjectTypeTabInfo.MetadataTab.DDL);
		}
		
		for(ObjectTypeTabInfo.MetadataTab mt : metabTab){
			tabList.add(mt);
		}
	}
		
	public String getName() {
		return name;
	}

	public String getContentid() {
		return contentid;
	}
	
	public List<ObjectTypeTabInfo.MetadataTab> getTabList() {
		return tabList;
	}
}
