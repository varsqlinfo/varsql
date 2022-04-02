package com.varsql.core.db.util;

import com.varsql.core.db.datatype.DataType;
import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : DbMetaUtils.java
 * @프로그램 설명 : type util
 * @Date      : 2018. 4. 5.
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class DbMetaUtils {
	/**
	 * @method  : getDefaultValue
	 * @desc : ddl default value
	 * @author   : ytkim
	 * @date   : 2021. 3. 2.
	 * @param columnDef
	 * @param dataTypeInfo
	 * @param onlyChar
	 * @return
	 */
	public static String getDefaultValue(String columnDef, DataType dataTypeInfo, boolean onlyChar) {

		if(StringUtils.isBlank(columnDef)) {
			return "";
		}

		if(columnDef.toUpperCase().startsWith("DEFAULT")){
			return columnDef;
		}else{
			if(!onlyChar){
				if (dataTypeInfo.getJDBCDataTypeMetaInfo().isString() && !columnDef.startsWith("'")){
					columnDef = "'"+columnDef+"'";
				}
			}
			return " DEFAULT " +columnDef;
		}
	}
	/**
	 * @method  : getNotNullValue
	 * @desc : ddl null value
	 * @author   : ytkim
	 * @date   : 2021. 3. 2.
	 * @param nullable
	 * @return
	 */
	public static String getNotNullValue(String nullable) {
		if ("NO".equalsIgnoreCase(nullable) || "N".equalsIgnoreCase(nullable)) {
			return " NOT NULL ";
		}
		return "";
	}
}
