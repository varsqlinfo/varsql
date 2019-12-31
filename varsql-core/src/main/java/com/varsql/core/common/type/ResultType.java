package com.varsql.core.common.type;

/**
 * 
 * @FileName : SqlCommandType.java
 * @프로그램 설명 :
 * @Date : 2015. 4. 8.
 * @작성자 : ytkim
 * @변경이력 :
 */
public enum ResultType {
	SUCCESS("success"), FAIL("fail"), ERROR("error");

	private String type;

	ResultType(String _type) {
		this.type = _type;
	}

	public String val() {
		return this.type;
	}
}
