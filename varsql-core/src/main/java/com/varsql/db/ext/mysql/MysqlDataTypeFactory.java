package com.varsql.db.ext.mysql;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 * Mysql data type
 * @author User
 *
 */
public class MysqlDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public MysqlDataTypeFactory() {
		addDataType(VenderDataType.newCustomDataType(DefaultDataType.VARCHAR, VARCHAR_DEFAULT_SIZE));
		addDataType(new VenderDataType("TEXT", DefaultDataType.CLOB.getTypeCode() , DBColumnMetaInfo.TEXT));
		addDataType(new VenderDataType("TEXT", DefaultDataType.NCLOB.getTypeCode() , DBColumnMetaInfo.TEXT));
		addDataType(new VenderDataType("TIME", DefaultDataType.TIME_WITH_TIMEZONE.getTypeCode() , DBColumnMetaInfo.TIME));
		addDataType(new VenderDataType("TIMESTAMP", DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode() , DBColumnMetaInfo.DATE));
	}
}
