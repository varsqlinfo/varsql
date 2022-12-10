package com.varsql.db.ext.h2;

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
public class H2DataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public H2DataTypeFactory() {
		addDataType(new VenderDataType("CHARACTER VARYING", DefaultDataType.VARCHAR.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("VARCHAR_IGNORECASE", DefaultDataType.VARCHAR.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("CHARACTER LARGE OBJECT", DefaultDataType.CLOB.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("CHARACTER", DefaultDataType.CHAR.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("TIME WITH TIME ZONE", DefaultDataType.TIME.getTypeCode() , DBColumnMetaInfo.TIME));
		addDataType(new VenderDataType("TIMESTAMP WITH TIME ZONE", DefaultDataType.TIMESTAMP.getTypeCode() , DBColumnMetaInfo.DATE));
	}
}
