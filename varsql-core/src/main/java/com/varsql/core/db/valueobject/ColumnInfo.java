package com.varsql.core.db.valueobject;

import java.math.BigDecimal;

/**
 * 
 * @FileName  : ColumnInfo.java
 * @프로그램 설명 : grid 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ColumnInfo {
	private int no;  
	// 컬럼 명. 
	private String name; 
	
	//컬럼 타입
	private String dataType;
	
	//컬럼 타입명
	private String typeName;
	
	//컬럼 타입 & length
	private String typeAndLength;
	
	// 사이즈
	private BigDecimal length;
	
	// null 여부.
	private String nullable;
	
	// 코멘트
	private String comment;
	
	// 제약조건
	private String constraints;
	
	// 자동증가 여부.
	private String autoincrement;
	
	// default value
	private String defaultVal;

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

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}
	
	public void setLength(int length) {
		this.length = new BigDecimal(length);;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(String autoincrement) {
		this.autoincrement = autoincrement;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getTypeAndLength() {
		return typeAndLength;
	}

	public void setTypeAndLength(String typeAndLength) {
		this.typeAndLength = typeAndLength;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}
}

