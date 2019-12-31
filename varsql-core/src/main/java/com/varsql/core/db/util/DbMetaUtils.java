package com.varsql.core.db.util;

import com.varsql.core.db.beans.ColumnInfo;
import com.varsql.core.db.beans.DataTypeInfo;

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
		
		//System.out.println(typeName+" :: "+  dataTypeInfo.getDbType() +" >> "+columnSize +" :: " + degitsLen);
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
}
