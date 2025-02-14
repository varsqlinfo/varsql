package com.varsql.core.db.util;

import java.sql.SQLException;
import java.util.List;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
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
			if(DefaultDataType.OTHER.equals(dataTypeInfo)) {
				if(!StringUtils.isNumber(columnDef)) {
					columnDef = "'"+columnDef+"'";
				}
			}else if (dataTypeInfo.getJDBCDataTypeMetaInfo().isString() && !columnDef.startsWith("'")){
				columnDef = "'"+columnDef+"'";
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
	
	/**
	 * standard data type
	 * 
	 * @example
	 * TIMESTAMP(6) -> TIMESTAMP
	 * 
	 * @param typeName
	 * @return
	 */
	public static String getTypeName(String typeName) {
		if(typeName.indexOf("(") > -1) {
			return typeName.replaceAll("\\(.*\\)", "").trim();
		}
		return typeName;
	}
		
	/**
	 * 스키마 목록
	 * @param dpi
	 * @return
	 * @throws SQLException
	 */
	public static List<String> schemaList(DatabaseParamInfo dpi) throws SQLException {
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());
		
		DBVenderType venderType = DBVenderType.getDBType(dpi.getType());
		
		if(venderType.isUseDatabaseName()) {
			return dbMetaEnum.getDatabases(dpi);
		}else {
			return dbMetaEnum.getSchemas(dpi);
		}
	}
	
	public static boolean isSchemaView(DatabaseParamInfo dataParamInfo) {
		return dataParamInfo.isSchemaViewYn();
	}

}
