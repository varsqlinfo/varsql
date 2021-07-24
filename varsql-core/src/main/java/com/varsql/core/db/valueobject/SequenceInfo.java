package com.varsql.core.db.valueobject;

import org.apache.ibatis.type.Alias;

/**
 * 
 * @FileName  : SequenceInfo.java
 * @프로그램 설명 : Sequence 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Alias("sequenceInfo")
public class SequenceInfo extends BaseObjectInfo{
	// sequence 명. 
	private String minValue;
	private String maxValue;
	private String cacheSize;
	//생성일
	private String created;
	// 상태
	private String status;
	private String cycleFlag;
	private String orderFlag;
	
	private String lastDdlTime;
	private String incrementBy;
	private String lastNumber;
	
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getCacheSize() {
		return cacheSize;
	}
	public void setCacheSize(String cacheSize) {
		this.cacheSize = cacheSize;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCycleFlag() {
		return cycleFlag;
	}
	public void setCycleFlag(String cycleFlag) {
		this.cycleFlag = cycleFlag;
	}
	public String getOrderFlag() {
		return orderFlag;
	}
	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}
	public String getLastDdlTime() {
		return lastDdlTime;
	}
	public void setLastDdlTime(String lastDdlTime) {
		this.lastDdlTime = lastDdlTime;
	}
	public String getIncrementBy() {
		return incrementBy;
	}
	public void setIncrementBy(String incrementBy) {
		this.incrementBy = incrementBy;
	}
	public String getLastNumber() {
		return lastNumber;
	}
	public void setLastNumber(String lastNumber) {
		this.lastNumber = lastNumber;
	}
	
}

