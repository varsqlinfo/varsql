package com.varsql.web.dto.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String viewid;

	private String uid;

	private String uname;

	private String orgNm;

	private String deptNm;

	private String lang;

	private String uemail;
	
	private String mobileNo;
	
	private String telNo;
	
	private String empNo;

	private String userRole;

	private String description;

	private boolean acceptYn;

	private boolean blockYn;

	private String regId;

	private LocalDateTime regDt;

}