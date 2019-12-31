package com.varsql.app.user.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.varsql.app.user.beans.valid.ValidPassword;

public class JoinForm extends UserForm{
	
	
	@NotBlank
	@Size(max=250)
	private String uid;
	
	@NotBlank
	@Size(max=500)
	@ValidPassword
	private String upw;
	
	@NotBlank
	@Size(max=500)
	private String confirmUpw;

	private String acceptYn;
	
	private String creId;
	
	@Size(max=1000)
	private String role;
	
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public String getConfirmUpw() {
		return confirmUpw;
	}

	public void setConfirmUpw(String confirmUpw) {
		this.confirmUpw = confirmUpw;
	}
	
}
