package com.varsql.app.admin.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: Vtconnection.java
* @DESC		: 커넥션 정보. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 2. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class Vtconnection{
	@Size(max=5)
	private String vconnid;
	
	@NotBlank
	@Size(max=250)
	private String vname;
	
	@NotBlank
	@Size(max=250)
	private String vdbschema;
	
	@NotBlank
	@Size(max=250)
	private String vurl;
	
	@NotBlank
	@Size(max=100)
	private String vdriver;
	@NotBlank
	@Size(max=100)
	private String vtype;

	@Size(max=100)
	private String vid;

	@Size(max=500)
	private String vpw;
	
	@Size(max=500)
	private String confirmPw;

	private String vdbversion;

	private String userId;
	
	private String poolInit;
	
	@NotBlank
	@Size(max=1)
	private String useYn;
	
	@NotBlank
	@Size(max=1)
	private String basetableYn;

	@NotBlank
	@Size(max=1)
	private String lazyloadYn;

	public String getVconnid(){
		return this.vconnid;
	}
	public void setVconnid(String vconnid){
		this.vconnid=vconnid;
	}
	public String getVname(){
		return this.vname;
	}
	public void setVname(String vname){
		this.vname=vname;
	}
	public String getVdbschema(){
		return this.vdbschema;
	}
	public void setVdbschema(String vdbschema){
		this.vdbschema=vdbschema;
	}
	public String getVurl(){
		return this.vurl;
	}
	public void setVurl(String vurl){
		this.vurl=vurl;
	}
	public String getVdriver(){
		return this.vdriver;
	}
	public void setVdriver(String vdriver){
		this.vdriver=vdriver;
	}
	public String getVtype(){
		return this.vtype;
	}
	public void setVtype(String vtype){
		this.vtype=vtype;
	}
	public String getVid(){
		return this.vid;
	}
	public void setVid(String vid){
		this.vid=vid;
	}
	public String getVpw(){
		return this.vpw;
	}
	public void setVpw(String vpw){
		this.vpw=vpw;
	}
	public String getVdbversion(){
		return this.vdbversion;
	}
	public void setVdbversion(String vdbversion){
		this.vdbversion=vdbversion;
	}
	
	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String regId){
		this.userId=regId;
	}
	public String getPoolInit() {
		return this.poolInit;
	}
	public void setPoolInit(String poolInit) {
		this.poolInit = poolInit;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getBasetableYn() {
		return basetableYn;
	}
	public void setBasetableYn(String basetableYn) {
		this.basetableYn = basetableYn;
	}
	public String getLazyloadYn() {
		return lazyloadYn;
	}
	public void setLazyloadYn(String lazyloadYn) {
		this.lazyloadYn = lazyloadYn;
	}
	

}