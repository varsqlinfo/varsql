package com.varsql.web.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserConnectionResponseDTO{
	private String vconnid;

	private String vname;

	private String blockYn;

	private boolean manager;

	@Builder
	public UserConnectionResponseDTO(String vconnid, String vname, String blockYn, Boolean manager) {
		this.vconnid = vconnid;
		this.vname = vname;
		this.blockYn = blockYn;
		this.manager = manager;
	}

	public String getBlockYn() {
		return blockYn == null?"N" :"Y";
	}

}