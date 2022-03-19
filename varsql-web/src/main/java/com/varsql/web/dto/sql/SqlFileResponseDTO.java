package com.varsql.web.dto.sql;

import java.time.LocalDateTime;

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

	private String sqlParam;

	private LocalDateTime regDt;

	@Builder
	public SqlFileResponseDTO(String sqlId, String sqlTitle, String sqlParam, LocalDateTime regDt) {
		this.sqlId = sqlId;
		this.sqlTitle = sqlTitle;
		this.sqlParam = sqlParam;
		this.regDt = regDt;
	}

}