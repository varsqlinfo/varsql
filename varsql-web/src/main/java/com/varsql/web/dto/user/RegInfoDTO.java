package com.varsql.web.dto.user;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RegInfoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String viewid;
	
	private String uid;

	private String uname;
	
	@Builder
	public RegInfoDTO(String viewid, String uid, String uname) {
		this.viewid = viewid; 
		this.uid = uid; 
		this.uname = uname; 
	}
}