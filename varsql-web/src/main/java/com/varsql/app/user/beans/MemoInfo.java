package com.varsql.app.user.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: MemoInfo.java
* @DESC		: 메모 정보. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 5. 2. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class MemoInfo{
	private String memoId;
	
	@NotBlank
	@Size(max=1000)
	private String memoTitle;
	
	private String memoCont;
	
	private String parentMemoId;
	
	private String reMemoCont;

	private char delYn;

	private String regId;
	
	@NotBlank
	private String recvId;

	public String getMemoId(){
		return this.memoId;
	}
	public void setMemoId(String memoId){
		this.memoId=memoId;
	}
	public String getMemoTitle(){
		return this.memoTitle;
	}
	public void setMemoTitle(String memoTitle){
		this.memoTitle=memoTitle;
	}
	public String getMemoCont(){
		return this.memoCont;
	}
	public void setMemoCont(String memoCont){
		this.memoCont=memoCont;
	}
	public char getDelYn(){
		return this.delYn;
	}
	public void setDelYn(char delYn){
		this.delYn=delYn;
	}
	public String getRegId(){
		return this.regId;
	}
	public void setRegId(String regId){
		this.regId=regId;
	}
	public String getRecvId() {
		return recvId;
	}
	public void setRecvId(String recvId) {
		this.recvId = recvId;
	}
	public String getReMemoCont() {
		return reMemoCont;
	}
	public void setReMemoCont(String reMemoCont) {
		this.reMemoCont = reMemoCont;
	}
	public String getParentMemoId() {
		return parentMemoId;
	}
	public void setParentMemoId(String parentMemoId) {
		this.parentMemoId = parentMemoId;
	}
}