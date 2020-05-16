package com.varsql.web.dto.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SqlFileResponseDTO{
	private String sqlId;
	
	private String vconnid;
	
	private String vname;

	private String viewid;

	private String gueryTitle;

	private String queryCont;

	private String sqlParam;

}