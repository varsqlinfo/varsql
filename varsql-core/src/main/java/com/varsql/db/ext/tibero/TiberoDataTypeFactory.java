package com.varsql.db.ext.tibero;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 * 
 * @FileName  : TiberoDataType.java
 * @프로그램 설명 : Tibero data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class TiberoDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public TiberoDataTypeFactory() {
		addDataType(new VenderDataType("TIMESTAMP WITH TIME ZONE", -101, DBColumnMetaInfo.OTHER));

		addDataType(new VenderDataType("CLOB", 2005, DBColumnMetaInfo.CLOB));
		addDataType(new VenderDataType("RAW", DefaultDataType.OTHER.getTypeCode(), DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("TIMESTAMP", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
		
		addDataType(new VenderDataType("VARCHAR2", 12, DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("NUMBER", 3, DBColumnMetaInfo.INTEGER));
	}
}
