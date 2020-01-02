package com.varsql.core.db.beans;

import java.util.List;

/**
 * 
 * @FileName  : TableInfo.java
 * @프로그램 설명 : 테이블 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class TableInfo extends BaseObjectInfo{
	
	private List<ColumnInfo> colList;

	public List<ColumnInfo> getColList() {
		return colList;
	}

	public void setColList(List<ColumnInfo> colList) {
		this.colList = colList;
	}
	
	public void addColInfo(ColumnInfo colInfo) {
		this.colList.add(colInfo);
	}
}
