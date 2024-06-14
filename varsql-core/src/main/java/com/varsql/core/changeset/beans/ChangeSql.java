package com.varsql.core.changeset.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangeSql {
	private String description; 
	private boolean multiple;
	private String delimiter;
	private String sql; 
	private String log; 
	
	@Override
	public String toString() {
		return sql;
	}
}
