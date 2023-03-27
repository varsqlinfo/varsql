package com.varsql.web.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserModReqeustDTO{
	
	@NotBlank
	@Size(max=250)
	private String uname;

	@Size(max=250)
	private String orgNm;

	@Size(max=250)
	private String deptNm;
	
	@Size(max=20)
	private String mobileNo;
	
	@Size(max=20)
	private String telNo;
	
	@Size(max=20)
	private String empNo;
	
	@Size(max=5)
	private String lang;

	@Size(max=2000)
	private String description;

}
