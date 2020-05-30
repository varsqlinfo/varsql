package com.varsql.db.ext.cubrid;

import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.varsql.core.db.valueobject.VarSqlDbType;

/**
 * 
 * @FileName  : DataTypeDB2.java
 * @프로그램 설명 : db2 data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class CubridDataType extends DataTypeImpl{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public CubridDataType() {
		// TODO Auto-generated constructor stub
		setDataTypeInfo("DATETIME", new  DataTypeInfo("TIMESTAMP", false , false, false, VarSqlDbType.DATE));
		
	}
}
