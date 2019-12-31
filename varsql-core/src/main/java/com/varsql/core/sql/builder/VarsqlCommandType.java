package com.varsql.core.sql.builder;


/**
 * 
 * @FileName : SqlCommandType.java
 * @프로그램 설명 :
 * @Date : 2015. 4. 8.
 * @작성자 : ytkim
 * @변경이력 :
 */
public enum VarsqlCommandType {
	SELECT("select"), UPDATE("update"), INSERT("insert"), DELETE("delete"), CREATE("create"), ALTER("alter"),DROP("drop"), TRUNCATE("truncate"), MERGE("merge"), REPLACE("replace"),CALL("call"), UNKNOWN("unknown");

	private String type;

	VarsqlCommandType(String _type) {
		this.type = _type;
	}

	public String val() {
		return this.type;
	}
}
