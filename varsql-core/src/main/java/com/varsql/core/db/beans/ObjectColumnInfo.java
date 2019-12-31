package com.varsql.core.db.beans;

/**
 * 
 * @FileName  : ObjectColumnInfo.java
 * @프로그램 설명 : object 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ObjectColumnInfo {
	private int no;  
	// 컬럼 명. 
	private String name; 
	
	//데이타 타입
	private String dataType;
	
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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

