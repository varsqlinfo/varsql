package com.varsql.db.ext.mariadb;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
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
		
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TEXT").typeCode(DefaultDataType.CLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("NTEXT").typeCode(DefaultDataType.NCLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIME").typeCode(DefaultDataType.TIME_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP").typeCode(DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.DATE).build()));
	
	}
}
