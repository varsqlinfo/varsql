package com.varsql.db.ext.mariadb;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 * Mariadb data type
 * @author User
 *
 */
public class MariadbDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public MariadbDataTypeFactory() {
		addDataType(new VenderDataType("TEXT", DefaultDataType.CLOB.getTypeCode() , DBColumnMetaInfo.TEXT));
		addDataType(new VenderDataType("TEXT", DefaultDataType.NCLOB.getTypeCode() , DBColumnMetaInfo.TEXT));
		addDataType(new VenderDataType("TIME", DefaultDataType.TIME_WITH_TIMEZONE.getTypeCode() , DBColumnMetaInfo.TIME));
		addDataType(new VenderDataType("TIMESTAMP", DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode() , DBColumnMetaInfo.DATE));
	}
}
