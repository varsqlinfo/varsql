package com.varsql.core.sql;

public enum ConstraintType {
	PRIMARY("PK"), UNIQUE("UQ"), FOREIGN("FK"), CHECK("CHK"), DEFAULT("DEFAULT");

	private String type;
	
	ConstraintType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public boolean typeEquals(String type) {
		return this.type.equalsIgnoreCase(type); 
	}
}
