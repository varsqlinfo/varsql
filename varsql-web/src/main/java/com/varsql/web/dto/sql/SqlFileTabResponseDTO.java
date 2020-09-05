package com.varsql.web.dto.sql;

import javax.persistence.Lob;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SqlFileTabResponseDTO{
	private String sqlId;

	private String prevSqlId;

	private boolean viewYn;

	private String sqlTitle;

	@Lob
	private String sqlCont;

	private String sqlParam;

	@Builder
	public SqlFileTabResponseDTO(String sqlId, String prevSqlId, boolean viewYn, String sqlTitle, String sqlCont, String sqlParam) {
		this.sqlId = sqlId;
		this.prevSqlId = prevSqlId;
		this.viewYn = viewYn;
		this.sqlTitle = sqlTitle;
		this.sqlCont = sqlCont;
		this.sqlParam = sqlParam;
	}

}