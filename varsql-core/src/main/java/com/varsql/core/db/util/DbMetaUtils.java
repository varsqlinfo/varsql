package com.varsql.core.db.util;

import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DataTypeInfo;
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
	 *
	 * @Method Name  : getTypeName
	 * @Method 설명 :
	 * @작성일   : 2018. 4. 5.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param dataTypeInfo
	 * @param column
	 * @param typeName
	 * @param degitsLen
	 * @param numPrecRadix
	 * @param columnSize
	 * @return
	 */

	public static String getTypeName(DataTypeInfo dataTypeInfo, ColumnInfo column, String typeName, String columnSize) {
		return getTypeName(dataTypeInfo, column, typeName, columnSize, null );
	}

	public static String getTypeName(DataTypeInfo dataTypeInfo, ColumnInfo column, String typeName, String columnSize, String degitsLen) {

		if(dataTypeInfo.isSizeYn() && columnSize !=null &&  !"".equals(columnSize)){

			String addStr =typeName+"(" + columnSize;

			if(dataTypeInfo.isRange()){
				if (degitsLen != null && !"".equals(degitsLen) && (Integer.parseInt(degitsLen)  > 0)) {
					addStr +="," + degitsLen;
				}
			}

			addStr+=")";
			return addStr;
		}
		return typeName;
	}

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
	public static String getDefaultValue(String columnDef ,DataTypeInfo dataTypeInfo, boolean onlyChar) {

		if(StringUtils.isBlank(columnDef)) {
			return "";
		}

		if(columnDef.toUpperCase().startsWith("DEFAULT")){
			return columnDef;
		}else{
			if(!onlyChar){
				if (dataTypeInfo.getDbType().isString() && !columnDef.startsWith("'")){
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
