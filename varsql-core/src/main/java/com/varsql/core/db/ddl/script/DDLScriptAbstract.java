package com.varsql.core.db.ddl.script;

import com.varsql.core.db.valueobject.DataTypeInfo;

/**
 * 
 * @FileName  : DDLScriptAbstract.java
 * @프로그램 설명 : script 생성 클래스
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public abstract class DDLScriptAbstract implements DDLScript{
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
		if (columnDef != null && !"".equals(columnDef)) {
			if(!onlyChar){
				if (dataTypeInfo.getDbType().isString() && !columnDef.startsWith("'")){
					columnDef = "'"+columnDef+"'";
				}
			}
			return " DEFAULT " +columnDef;
		}
		return "";
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
		if ("NO".equalsIgnoreCase(nullable) || "N".equalsIgnoreCase(nullable)) {
			return " NOT NULL ";
		}
		return "";
	}
}
