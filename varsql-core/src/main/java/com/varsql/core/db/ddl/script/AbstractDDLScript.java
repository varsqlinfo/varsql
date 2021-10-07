package com.varsql.core.db.ddl.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;

/**
 *
 * @FileName  : AbstractDDLScript.java
 * @프로그램 설명 : script 생성 클래스
 * @Date      : 2015. 6. 18.
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class AbstractDDLScript implements DDLScript{
	/**
	 *
	 * @Method Name  : getDefaultValue
	 * @Method 설명 :
	 * @작성일   : 2017. 11. 1.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param columnInfo
	 * @param key
	 * @param dataTypeInfo
	 * @param b
	 * @return
	 */
	protected String getDefaultValue(String columnDef ,DataTypeInfo dataTypeInfo) {
		return getDefaultValue(columnDef, dataTypeInfo, false);
	}
	protected String getDefaultValue(String columnDef ,DataTypeInfo dataTypeInfo, boolean onlyChar) {
		return DbMetaUtils.getDefaultValue(columnDef, dataTypeInfo, onlyChar);
	}
	/**
	 *
	 * @Method Name  : getNotNullValue
	 * @Method 설명 :
	 * @작성일   : 2017. 11. 1.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param source
	 * @param key
	 * @param dataTypeInfo
	 * @return
	 */
	protected String getNotNullValue(String nullable) {
		return DbMetaUtils.getNotNullValue(nullable);
	}

	protected Map getDefaultTemplateParam(DDLCreateOption ddlOption, DatabaseParamInfo dataParamInfo, List listMap) {
		Map param =  new HashMap();
		param.put("ddlOpt", ddlOption);
		param.put("dbType", dataParamInfo.getDbType());
		param.put("schema", dataParamInfo.getSchema());
		param.put("objectName", dataParamInfo.getObjectName());
		param.put("items", listMap);

		return param;
	}

	protected Map getDefaultTableTemplateParam(DDLCreateOption ddlOption, DatabaseParamInfo dataParamInfo, List columnList, List keyList, List commentsList) {
		Map param =  new HashMap();
		param.put("ddlOpt", ddlOption);
		param.put("dbType", dataParamInfo.getDbType());
		param.put("schema", dataParamInfo.getSchema());
		param.put("objectName", dataParamInfo.getObjectName());
		param.put("columnList", columnList);
		param.put("keyList", keyList);
		param.put("commentsList", commentsList);

		return param;
	}


}
