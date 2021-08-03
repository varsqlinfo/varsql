package com.varsql.db.ext.sybase;

import com.varsql.core.db.VarSqlDbDataType;
import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.valueobject.DataTypeInfo;

/**
 *
 * @FileName  : SybaseDataType.java
 * @프로그램 설명 : sybase data type
 * @Date      : 2021. 2. 06.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SybaseDataType extends DataTypeImpl{

	// 버전별 데이타를 체크 하기위해서 버전을 받음.
	public SybaseDataType() {
		// TODO Auto-generated constructor stub
		setDataTypeInfo("DATETIME", new  DataTypeInfo("TIMESTAMP", false , false, false, VarSqlDbDataType.DATE));

	}
}
