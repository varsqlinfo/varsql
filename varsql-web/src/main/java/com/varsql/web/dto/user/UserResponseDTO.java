package com.varsql.web.dto.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String viewid;

	private String uid;

	private String uname;

	private String orgNm;

	private String deptNm;

	private String lang;

	private String uemail;

	private String userRole;

	private String description;

	private boolean acceptYn;

	private boolean blockYn;

	private String regId;

	private String regDt;

}