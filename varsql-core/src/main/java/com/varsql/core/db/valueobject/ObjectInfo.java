package com.varsql.core.db.valueobject;

import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * 
 * @FileName  : ObjectInfo.java
 * @프로그램 설명 : db object 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Alias("objectInfo")
public class ObjectInfo extends BaseObjectInfo{
	// type
	private String type;
	
	private String returnType;
	
	// status
	private String status;
	
	// create date
	private String created;
	
	private List<ObjectColumnInfo> colList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}

