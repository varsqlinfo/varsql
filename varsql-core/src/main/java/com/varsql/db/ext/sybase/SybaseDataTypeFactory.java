package com.varsql.db.ext.sybase;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 *
 * @FileName  : SybaseDataType.java
 * @프로그램 설명 : sybase data type
 * @Date      : 2021. 2. 06.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SybaseDataTypeFactory extends AbstractDataTypeFactory{

	// 버전별 데이타를 체크 하기위해서 버전을 받음.
	public SybaseDataTypeFactory() {
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("DATETIME").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
	}
}
