package com.varsql.db.ext.cubrid;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;
import com.varsql.core.db.meta.MetaBeanConfig;

/**
 * 
 * @FileName  : CubridDataTypeFactory.java
 * @프로그램 설명 : db2 data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@MetaBeanConfig(dbVenderType = DBVenderType.CUBRID, metaBean = CubridDBMeta.class, ddlBean = CubridDDLScript.class, dataTypeBean = CubridDataTypeFactory.class)
public class CubridDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public CubridDataTypeFactory() {
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("STRING").typeCode(DefaultDataType.VARCHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).enableDefaultTypeName(true).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMPLTZ").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMPTZ").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("DATETIMELTZ").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("DATETIMETZ").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("SET").typeCode(DefaultDataType.ARRAY.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.ARRAY).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("MULTISET").typeCode(DefaultDataType.ARRAY.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.ARRAY).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("LIST").typeCode(DefaultDataType.ARRAY.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.ARRAY).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("SEQUENCE").typeCode(DefaultDataType.ARRAY.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.ARRAY).build()));
		
	}
}
