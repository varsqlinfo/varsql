package com.varsql.app.user.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: QnAInfo.java
* @DESC		: qna info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 1. 4. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class QnAInfo {

	@Size(max=32)
	private String qnaid;
	
	@NotBlank
	@Size(max=250)
	private String title;
	
	@NotBlank
	@Size(max=2000)
	private String question;

	@Size(max=2000)
	private String answer;

	@Size(max=1)
	private String delYn;
	
	private String userid;
	
	public String getQnaid(){
		return this.qnaid;
	}
	public void setQnaid(String qnaid){
		this.qnaid=qnaid;
	}
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public String getQuestion(){
		return this.question;
	}
	public void setQuestion(String question){
		this.question=question;
	}
	public String getAnswer(){
		return this.answer;
	}
	public void setAnswer(String answer){
		this.answer=answer;
	}
	public String getDelYn(){
		return this.delYn;
	}
	public void setDelYn(String delYn){
		this.delYn=delYn;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
