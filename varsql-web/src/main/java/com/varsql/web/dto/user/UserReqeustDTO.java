package com.varsql.web.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.varsql.web.dto.valid.ValidPassword;
import com.varsql.web.model.entity.user.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReqeustDTO{
	
	@NotBlank
	@Size(max=250)
	private String uid;
	
	@Email
	@NotBlank
	@Size(max=250)
	private String uemail;
	
	@Size(max=20)
	private String mobileNo;
	
	@Size(max=20)
	private String telNo;
	
	@Size(max=0)
	private String empNo;
	
	@NotBlank
	@Size(max=250)
	private String uname;

	@Size(max=250)
	private String orgNm;

	@Size(max=250)
	private String deptNm;
	
	@Size(max=5)
	private String lang;

	@Size(max=2000)
	private String description;
	
	@NotBlank
	@Size(max=500)
	@ValidPassword
	private String upw;
	
	@NotBlank
	@Size(max=500)
	private String confirmUpw;
	
	public UserEntity toEntity() {
		return UserEntity.builder()
				.uid(uid)
				.uname(uname)
				.uemail(uemail)
				.mobileNo(mobileNo)
				.telNo(telNo)
				.empNo(empNo)
				.orgNm(orgNm)
				.deptNm(deptNm)
				.lang(lang)
				.description(description)
				.upw(upw).build();
				
	}

}
