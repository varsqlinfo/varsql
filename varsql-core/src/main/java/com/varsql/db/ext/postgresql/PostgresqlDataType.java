package com.varsql.db.ext.postgresql;

import com.varsql.core.db.VarSqlDbDataType;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.valueobject.DataTypeInfo;

/**
 * 
 * @FileName  : PostgresqlDataType.java
 * @프로그램 설명 : Postgresql data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class PostgresqlDataType extends DataTypeImpl{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public PostgresqlDataType() {
		// TODO Auto-generated constructor stub
		setDataTypeInfo("DATETIME", new  DataTypeInfo("TIMESTAMP", false , false, false, VarSqlDbDataType.DATE));
		
	}
}
