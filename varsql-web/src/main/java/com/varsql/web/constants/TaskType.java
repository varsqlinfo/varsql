package com.varsql.web.constants;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * task 
 * 
 * @author ytkim
 *
 */
public enum TaskType{
	
	SQL("SQL")
	,TRANSFER("TRANSFER");
	
	@JsonProperty
	private String typeName;
	
	@JsonProperty
	private String code; 

	TaskType(String name) {
		this.typeName = name;
		this.code = this.name();
	}

	public String getTypeName() {
		return typeName;
	}

	public String getCode() {
		return code;
	}
	
	public static TaskType getComponentType(String type) {
		for(TaskType cmpType : TaskType.values()) {
			if(cmpType.getTypeName().equals(type)) {
				return cmpType;
			}
		}
		return null; 
	}
}
