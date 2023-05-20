package com.varsql.core.sql;

public enum CommentType {
	TABLE("TBL"), COLUMN("COL");

	private String type;
	
	CommentType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
