package com.varsql.core.sql.builder;

/**
 * 
 * @FileName : StatementType.java
 * @프로그램 설명 :
 * @Date : 2015. 4. 8.
 * @작성자 : ytkim
 * @변경이력 :
 */
public enum VarsqlStatementType {
	PREPARED("prepared"), CALLABLE("call"), STATEMENT("statement");

	private String type;

	VarsqlStatementType(String _type) {
		this.type = _type;
	}

	public String val() {
		return this.type;
	}
}
