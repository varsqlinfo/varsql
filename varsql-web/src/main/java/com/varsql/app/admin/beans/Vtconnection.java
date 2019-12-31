package com.varsql.app.admin.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.varsql.app.admin.beans.valid.ValidUrlDirectYn;

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
@ValidUrlDirectYn
public class Vtconnection{
	@Size(max=5)
	private String vconnid;
	
	@NotBlank
	@Size(max=250)
	private String vname;
	
	@Size(max=45)
	private String vserverip;
	
	@Size(max=6)
	private String vport;
	
	@Size(max=250)
	private String vdatabasename;
	
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
	
	@Size(max=1)
	private String urlDirectYn;
	
	private String vdbversion;

	private String userId;
	
	private String poolInit;
	
	private String vdbschema;
	
	@NotBlank
	@Size(max=1)
	private String useYn;
	
	@NotBlank
	@Size(max=1)
	private String basetableYn;

	@NotBlank
	@Size(max=1)
	private String lazyloadYn;
	
	@NotBlank
	@Size(max=1)
	private String schemaViewYn;

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
	public String getSchemaViewYn() {
		return schemaViewYn;
	}
	public void setSchemaViewYn(String schemaViewYn) {
		this.schemaViewYn = schemaViewYn;
	}
	public String getVserverip() {
		return vserverip;
	}
	public void setVserverip(String vserverip) {
		this.vserverip = vserverip;
	}
	public String getVport() {
		return vport;
	}
	public void setVport(String vport) {
		this.vport = vport;
	}
	public String getVdatabasename() {
		return vdatabasename;
	}
	public void setVdatabasename(String vdatabasename) {
		this.vdatabasename = vdatabasename;
	}
	public String getUrlDirectYn() {
		return urlDirectYn;
	}
	public void setUrlDirectYn(String urlDirectYn) {
		this.urlDirectYn = urlDirectYn;
	}
	/**
	 * @return the vdbschema
	 */
	public String getVdbschema() {
		return vdbschema;
	}
	/**
	 * @param vdbschema the vdbschema to set
	 */
	public void setVdbschema(String vdbschema) {
		this.vdbschema = vdbschema;
	}
	
	
}