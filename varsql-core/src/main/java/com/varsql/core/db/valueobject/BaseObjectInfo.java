package com.varsql.core.db.valueobject;

/**
 * 
 * @FileName  : BaseObjectInfo.java
 * @프로그램 설명 : db object 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class BaseObjectInfo {
	// 테이블명. 
	private String name; 
	
	// 비고
	private String remarks;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}

