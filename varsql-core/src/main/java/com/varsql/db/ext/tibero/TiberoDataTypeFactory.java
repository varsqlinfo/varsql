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
		addDataType(new VenderDataType("TIMESTAMP WITH TIME ZONE", DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode(), DBColumnMetaInfo.OTHER));

		addDataType(new VenderDataType("CLOB", DefaultDataType.CLOB.getTypeCode(), DBColumnMetaInfo.CLOB));
		addDataType(new VenderDataType("RAW", DefaultDataType.OTHER.getTypeCode(), DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("TIMESTAMP", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
		
		addDataType(new VenderDataType("VARCHAR2", DefaultDataType.VARCHAR.getTypeCode(), DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("NUMBER", DefaultDataType.DECIMAL.getTypeCode(), DBColumnMetaInfo.BIGDECIMAL));
	}
}
