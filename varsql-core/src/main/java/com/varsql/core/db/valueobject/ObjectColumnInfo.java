package com.varsql.core.db.valueobject;

import org.apache.ibatis.type.Alias;

/**
 * 
 * @FileName  : ObjectColumnInfo.java
 * @프로그램 설명 : object 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Alias("objectColumnInfo")
public class ObjectColumnInfo {
	private int no;  
	// 컬럼 명. 
	private String name; 
	
	//데이타 cd
	private String typeCode;
	
	//데이타 타입
	private String typeName;
	
	//컬럼 타입
	private String columnType;
	
	// 코멘트
	private String comment;
	
	private String ascOrdesc;
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getAscOrdesc() {
		return ascOrdesc;
	}

	public void setAscOrdesc(String ascOrdesc) {
		this.ascOrdesc = ascOrdesc;
	}
}

