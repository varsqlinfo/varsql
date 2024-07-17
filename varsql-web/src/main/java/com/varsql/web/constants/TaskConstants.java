package com.varsql.web.constants;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * component 상수
* 
* @fileName	: ComponentConstants.java
* @author	: ytkim
 */
public interface TaskConstants{
	public String SQL_TYPE_NAME  = "SQL"; 
	public String TRANSFER_TYPE_NAME  = "TRANSFER"; 
	
	enum TYPE {
		SQL(SQL_TYPE_NAME)
		,TRANSFER(TRANSFER_TYPE_NAME);
		
		@JsonProperty
		private String typeName;
		
		@JsonProperty
		private String code; 

		TYPE(String name) {
			this.typeName = name;
			this.code = this.name();
		}

		public String getTypeName() {
			return typeName;
		}

		public String getCode() {
			return code;
		}
	}
	
	public static TYPE getComponentType(String type) {
		for(TYPE cmpType : TYPE.values()) {
			if(cmpType.getTypeName().equals(type)) {
				return cmpType;
			}
		}
		return null; 
	}
}
