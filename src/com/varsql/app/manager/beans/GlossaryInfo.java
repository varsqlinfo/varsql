package com.varsql.app.manager.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GlossaryInfo.java
* @DESC		: 용어 bean 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class GlossaryInfo{
	private String wordIdx;

	@NotBlank
	@Size(max=500)
	private String word;

	@NotBlank
	@Size(max=500)
	private String wordEn;

	@NotNull
	@Size(max=500)
	private String wordAbbr;

	@Size(max=20000)
	private String wordDesc;

	private String userId;

	public String getWordIdx(){
		return this.wordIdx;
	}
	public void setWordIdx(String wordIdx){
		this.wordIdx=wordIdx;
	}
	public String getWord(){
		return this.word;
	}
	public void setWord(String word){
		this.word=word;
	}
	public String getWordEn(){
		return this.wordEn;
	}
	public void setWordEn(String wordEn){
		this.wordEn=wordEn;
	}
	public String getWordAbbr(){
		return this.wordAbbr;
	}
	public void setWordAbbr(String wordAbbr){
		this.wordAbbr=wordAbbr;
	}
	public String getWordDesc(){
		return this.wordDesc;
	}
	public void setWordDesc(String wordDesc){
		this.wordDesc=wordDesc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}