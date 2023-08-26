package com.varsql.db.ext.cubrid;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 * 
 * @FileName  : CubridDataTypeFactory.java
 * @프로그램 설명 : db2 data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class CubridDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public CubridDataTypeFactory() {
		addDataType(new VenderDataType("TIMESTAMP", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
		addDataType(new VenderDataType("TIMESTAMPLTZ", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
		addDataType(new VenderDataType("TIMESTAMPTZ", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
		addDataType(new VenderDataType("DATETIMELTZ", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
		addDataType(new VenderDataType("DATETIMETZ", DefaultDataType.TIMESTAMP.getTypeCode(), DBColumnMetaInfo.TIMESTAMP));
	}
}
