package com.varsql.web.dto.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPermissionInfoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String viewid;

	private boolean acceptYn;

	private boolean blockYn;

	private String vconnid;

	public UserPermissionInfoDTO(String viewid, Boolean acceptYn, Boolean blockYn, String vconnid) {
		this.viewid = viewid;
		this.acceptYn = acceptYn;
		this.blockYn = blockYn;
		this.vconnid = vconnid;
	}
}