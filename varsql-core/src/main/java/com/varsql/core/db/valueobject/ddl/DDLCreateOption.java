package com.varsql.core.db.valueobject.ddl;

/**
 * 
 * @FileName  : DDLCreateOption.java
 * @프로그램 설명 : ddl create option
 * @Date      : 2018. 12. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DDLCreateOption {

	boolean addDropClause = true; // drop 절 추가 여부
	
	boolean addLastSemicolon = true; // 구문 끝  ; 콜론 추가 여부.

	public boolean isAddDropClause() {
		return addDropClause;
	}

	public void setAddDropClause(boolean addDropClause) {
		this.addDropClause = addDropClause;
	}

	public boolean isAddLastSemicolon() {
		return addLastSemicolon;
	}

	public void setAddLastSemicolon(boolean addLastSemicolon) {
		this.addLastSemicolon = addLastSemicolon;
	}
	
	
	
	
}

