package com.varsql.core.db.servicemenu;

/**
 * 
 * @FileName  : ObjectType.java
 * @프로그램 설명 : db service object
 * @Date      : 2019. 11. 26. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public enum DBObjectType implements ObjectType{
	TABLE("Table","table") 
	, VIEW("View","view") 
	, PROCEDURE("Procedure","procedure")
	, FUNCTION("Function","function")
	, PACKAGE("Package","package")
	, INDEX("Index","index")
	, SEQUENCE("Sequence","sequence")
	, TRIGGER("Trigger","trigger");
		
	private String objName; 
	private String objId; 
	 
	private DBObjectType(String name, String id){
		this.objName = name;  
		this.objId = id;  
	}
	
	public String getObjectTypeName() {
		return objName;
	}
	
	public String getObjectTypeId() {
		return objId;
	}
	
	public static DBObjectType getDBObjectType(String objNm) {
		try{
			return DBObjectType.valueOf(objNm.toUpperCase());
		}catch(IllegalArgumentException e){
			return null; 
		}
	}

}
