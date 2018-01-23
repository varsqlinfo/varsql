package com.varsql.web.app.admin.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

//@author
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

	@Size(max=1000)
	private String vquery;

	@Size(max=100)
	private String vid;

	@Size(max=500)
	private String vpw;

	@Size(max=2000)
	private String vconnopt;

	@Size(max=2000)
	private String vpoolopt;

	private int vdbversion;

	private String userId;
	
	private String pollinit;

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
	public String getVquery(){
		return this.vquery;
	}
	public void setVquery(String vquery){
		this.vquery=vquery;
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
	public String getVconnopt(){
		return this.vconnopt;
	}
	public void setVconnopt(String vconnopt){
		this.vconnopt=vconnopt;
	}
	public String getVpoolopt(){
		return this.vpoolopt;
	}
	public void setVpoolopt(String vpoolopt){
		this.vpoolopt=vpoolopt;
	}
	public int getVdbversion(){
		return this.vdbversion;
	}
	public void setVdbversion(int vdbversion){
		this.vdbversion=vdbversion;
	}
	
	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String regId){
		this.userId=regId;
	}
	public String getPollinit() {
		return pollinit;
	}
	public void setPollinit(String pollinit) {
		this.pollinit = pollinit;
	}

}