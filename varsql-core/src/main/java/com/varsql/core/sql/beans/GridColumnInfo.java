package com.varsql.core.sql.beans;

import com.varsql.core.common.constants.ColumnJavaType;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @FileName  : ColumnInfo.java
 * @프로그램 설명 : grid 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31.
 * @작성자      : ytkim
 * @변경이력 :
 */
@Getter
@Setter
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
	private ColumnJavaType type;

	// db type
	private String dbType;
	
	private int dbTypeCode;

	// 숫자 여부.
	private boolean number;

	// lob type 여부
	private boolean lob;
	
	public void setType(ColumnJavaType type) {
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = ColumnJavaType.getType(type);
	}

	@Override
	public String toString() {

		return new StringBuffer()
				.append("no : ").append(no)
				.append(", label : ").append(label)
				.append(" key : ").append(key)
				.append(" dbType : ").append(dbType)
				.append(" type : ").append(type)
				.toString();
	}
}

