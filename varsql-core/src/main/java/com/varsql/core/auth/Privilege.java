package com.varsql.core.auth;

/**
 * 
 * @FileName  : Privilege.java
 * @Date      : 2014. 8. 20. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
public class Privilege {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Privilege [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
