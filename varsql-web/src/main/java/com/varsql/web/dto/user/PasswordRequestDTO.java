package com.varsql.web.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.varsql.web.dto.valid.ValidPassword;

public class PasswordRequestDTO {
	
	private String viewid;
	
	private String currPw;
	
	@NotBlank
	@Size(max=500)
	@ValidPassword
	private String upw;
	
	@NotBlank
	@Size(max=500)
	private String confirmUpw;

	public String getUpw() {
		return upw;
	}

	public void setUpw(String upw) {
		this.upw = upw;
	}

	public String getCurrPw() {
		return currPw;
	}

	public void setCurrPw(String currPw) {
		this.currPw = currPw;
	}

	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	}

	public String getConfirmUpw() {
		return confirmUpw;
	}

	public void setConfirmUpw(String confirmUpw) {
		this.confirmUpw = confirmUpw;
	}
}