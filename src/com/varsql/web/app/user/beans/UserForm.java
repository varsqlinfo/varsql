package com.varsql.web.app.user.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;



public class UserForm {
	
	private String viewid;
	
	@NotBlank
	@Size(max=250)
	private String uid;
	
	@NotBlank
	@Size(max=250)
	private String uemail;
	
	@NotBlank
	@Size(max=500)
	@ValidPassword
	private String upw;
	
	@NotBlank
	@Size(max=500)
	private String configUpw;
	
	@NotBlank
	@Size(max=250)
	private String uname;

	@Size(max=250)
	private String orgNm;

	@Size(max=250)
	private String deptNm;

	@Size(max=1000)
	private String role;

	@Size(max=2000)
	private String description;
	
	private String acceptYn;
	
	private String creId;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUpw() {
		return upw;
	}

	public void setUpw(String upw) {
		this.upw = upw;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	public String getUemail() {
		return uemail;
	}

	public void setUemail(String uemail) {
		this.uemail = uemail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	}

	public String getAcceptYn() {
		return acceptYn;
	}

	public void setAcceptYn(String acceptYn) {
		this.acceptYn = acceptYn;
	}

	public String getCreId() {
		return creId;
	}

	public void setCreId(String creId) {
		this.creId = creId;
	}

	public String getConfigUpw() {
		return configUpw;
	}

	public void setConfigUpw(String configUpw) {
		this.configUpw = configUpw;
	}
}
