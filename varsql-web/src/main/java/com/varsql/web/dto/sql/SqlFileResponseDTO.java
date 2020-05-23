package com.varsql.web.dto.sql;

import lombok.Builder;
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

	private String sqlTitle;

	private String sqlCont;

	private String sqlParam;
	
	@Builder
	public SqlFileResponseDTO(String sqlId, String sqlTitle, String sqlCont, String sqlParam) {
		this.sqlId = sqlId;
		this.sqlTitle = sqlTitle;
		this.sqlCont = sqlCont;
		this.sqlParam = sqlParam;
	}

}