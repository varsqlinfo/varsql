package com.varsql.core.sql;

import com.varsql.core.db.servicemenu.ObjectType;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : SQL.java
 * @프로그램 설명 : SQL template infomation
 * @Date      : 2020. 11. 26.
 * @작성자      : ytkim
 * @변경이력 :
 */
public enum SQL {
	
	// ddl
	CREATE (SQLType.DDL),
	DROP (SQLType.DDL),
	TRUNCATE  (SQLType.DDL),
	ALTER_RENAME_TABLE (SQLType.DDL),
	
	ALTER_ADD_COLUMN (SQLType.DDL),
	ALTER_MODIFY_COLUMN (SQLType.DDL),
	ALTER_DROP_COLUMN (SQLType.DDL),
	ALTER_RENAME_COLUMN (SQLType.DDL),
	
	//dml
	SELECT (SQLType.DML),
	INSERT (SQLType.DML),
	UPDATE (SQLType.DML),
	DELETE (SQLType.DML),
	
	//dcl
	GRANT (SQLType.DCL),
	REVOKE (SQLType.DCL);
	
	private String templateId; 
	private SQLType sqlType; 
	
	SQL(SQLType sqlType){
		this.sqlType = sqlType;
		
		if(sqlType == SQLType.DDL) {
			this.templateId = StringUtils.capitalize(StringUtils.camelCase(this.name()));
		}else if(sqlType == SQLType.DML) {
			this.templateId = StringUtils.capitalize(this.name());
		}else if(sqlType == SQLType.DCL) {
			this.templateId = StringUtils.capitalize(this.name());
		}
	}
	
	public String getTemplateId() {
		return this.templateId; 
	}

	public String getTemplateId(ObjectType objectType) {
		if(this.sqlType == SQLType.DDL) {
			return objectType.name().toLowerCase()+ this.templateId;
		}else {
			return this.templateId; 
		}
	}
		
}
