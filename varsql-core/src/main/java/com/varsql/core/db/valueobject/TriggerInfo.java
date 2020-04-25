package com.varsql.core.db.valueobject;

/**
 * 
 * @FileName  : TriggerInfo.java
 * @프로그램 설명 : trigger 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class TriggerInfo extends BaseObjectInfo{
	// 테이블명
	private String tblName;
	// 이벤트 타입
	private String eventType;
	// 타이밍
	private String timing; 
	// 상태
	private String created;
	// 상태
	private String status;

	public String getTblName() {
		return tblName;
	}

	public void setTblName(String tblName) {
		this.tblName = tblName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
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
	
}

