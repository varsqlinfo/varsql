package com.varsql.web.dto.sql;

import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.web.model.entity.sql.SqlFileEntity;
import com.varsql.web.util.DatabaseUtils;
import com.varsql.web.util.SecurityUtil;

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
public class SqlFileRequestDTO{
	
	private String conuid;
	
	private String vconnid;

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
	
	public void setConuid(String conuid) {
		this.conuid = conuid; 
		this.vconnid = DatabaseUtils.convertConUidToVconnid(conuid);
	}

	public SqlFileEntity toEntity() {
		return SqlFileEntity.builder()
			.sqlId(this.sqlId)
			.sqlTitle(this.sqlTitle ==null ? "": this.sqlTitle)
			.sqlCont(this.sqlCont)
			.sqlParam(this.sqlParam)
			.vconnid(this.vconnid)
			.viewid(SecurityUtil.userViewId())
			.editorCursor(editorCursor)
			.build();
	}
}
