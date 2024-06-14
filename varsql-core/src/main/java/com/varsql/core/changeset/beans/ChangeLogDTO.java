package com.varsql.core.changeset.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangeLogDTO {
	private String id; 

	private String fileName;
	
	private String type; 

	private String version; 

	private String hash; 

	private String appliedDt; 

	private String description; 

	private String applySql; 

	private String revertSql; 

	private String state; 

	private String sqlLog; 
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("id : ").append(id).append(", ")
				.append("fileName : ").append(fileName).append(", ")
				.append("type : ").append(type).append(", ")
				.append("version : ").append(version).append(", ")
				.append("hash : ").append(hash).append(", ")
				.append("appliedDt : ").append(appliedDt).append(", ")
				.append("description : ").append(description).append(", ")
				.append("state : ").append(state).append(", ")
				.toString();
	}
}
