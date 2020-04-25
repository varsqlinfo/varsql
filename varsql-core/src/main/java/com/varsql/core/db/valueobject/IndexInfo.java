package com.varsql.core.db.valueobject;

import java.util.List;

/**
 * 
 * @FileName  : IndexInfo.java
 * @프로그램 설명 : index 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class IndexInfo extends BaseObjectInfo{
	// 테이블명
	private String tblName;
	// 타입 (pk ,fk ,uni)
	private String type;
	// 테이블명
	private String tableSpace;
	// 버퍼불
	private String bufferPool; 
	// 상태
	private String status;
	
	private List<ObjectColumnInfo> colList;

	public String getTblName() {
		return tblName;
	}

	public void setTblName(String tblName) {
		this.tblName = tblName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTableSpace() {
		return tableSpace;
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public String getBufferPool() {
		return bufferPool;
	}

	public void setBufferPool(String bufferPool) {
		this.bufferPool = bufferPool;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ObjectColumnInfo> getColList() {
		return colList;
	}

	public void setColList(List<ObjectColumnInfo> colList) {
		this.colList = colList;
	}
	
	public void addColInfo(ObjectColumnInfo colInfo) {
		this.colList.add(colInfo);
	}
}

