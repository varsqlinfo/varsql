package com.varsql.core.sql.beans;

/**
 *
 * @FileName  : ColumnInfo.java
 * @프로그램 설명 : grid 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class GridColumnInfo {

	// grid idx
	private int no;

	// grid label
	private String label;

	// grid key
	private String key;

	// grid width
	private int width;

	// 정렬
	private String align;

	// varsql type
	private String type;

	// db type
	private String dbType;

	// 숫자 여부.
	private boolean number;

	// lob type 여부
	private boolean lob;

	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isNumber() {
		return number;
	}
	public void setNumber(boolean number) {
		this.number = number;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public boolean isLob() {
		return lob;
	}
	public void setLob(boolean lob) {
		this.lob = lob;
	}

	@Override
	public String toString() {

		return new StringBuffer()
				.append("no : ").append(no)
				.append(", label : ").append(label)
				.append(" key : ").append(key)
				.toString();
	}
}

