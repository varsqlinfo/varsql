package com.varsql.web.dto.sql;

import javax.persistence.Lob;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SqlFileTabResponseDTO{
	private String sqlId;

	private String prevSqlId;

	private boolean viewYn;

	private String sqlTitle;

	@Lob
	private String sqlCont;

	private String sqlParam;

	private String editorCursor;

	@Builder
	public SqlFileTabResponseDTO(String sqlId, String prevSqlId, boolean viewYn, String sqlTitle, String sqlCont, String sqlParam, String editorCursor) {
		this.sqlId = sqlId;
		this.prevSqlId = prevSqlId;
		this.viewYn = viewYn;
		this.sqlTitle = sqlTitle;
		this.sqlCont = sqlCont;
		this.sqlParam = sqlParam;
		this.editorCursor = editorCursor;
	}

}