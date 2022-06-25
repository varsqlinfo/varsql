package com.varsql.db.ext.postgresql;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 * 
 * @FileName  : PostgresqlDataTypeFactory.java
 * @프로그램 설명 : Postgresql data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class PostgresqlDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public PostgresqlDataTypeFactory() {
		addDataType(new VenderDataType("DATETIME", DefaultDataType.TIMESTAMP.getTypeCode() , DBColumnMetaInfo.TIMESTAMP));
	}
}
