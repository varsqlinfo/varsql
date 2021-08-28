package com.varsql.web.dto.sql;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.web.model.entity.sql.SqlFileEntity;

import lombok.Getter;
import lombok.Setter;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlFileRequestDTO.java
* @DESC		: sql file info
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
public class SqlFileRequestDTO extends DatabaseParamInfo{

	// sql id
	private String sqlId;

	// move before prev id
	private String firstSqlId;

	private String prevSqlId;

	// sql title
	private String sqlTitle;

	// sql content
	private String sqlCont;

	private String editorCursor;

	// limit count
	private int limit;

	// sql parameer
	private String sqlParam;

	private String columnInfo;

	public SqlFileRequestDTO(){
		super();
	}

	public int getLimit() {
		return limit > -1 ? this.limit : SqlDataConstants.DEFAULT_LIMIT_ROW_COUNT;
	}

	public SqlFileEntity toEntity() {
		return SqlFileEntity.builder()
			.sqlId(sqlId)
			.sqlTitle(sqlTitle ==null ? "":sqlTitle)
			.sqlCont(sqlCont)
			.sqlParam(sqlParam)
			.vconnid(getVconnid())
			.viewid(getViewid())
			.editorCursor(editorCursor)
			.build();
	}
}
