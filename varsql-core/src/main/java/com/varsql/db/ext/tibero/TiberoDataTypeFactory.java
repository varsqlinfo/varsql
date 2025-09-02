package com.varsql.db.ext.tibero;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;
import com.varsql.core.db.meta.MetaBeanConfig;
import com.varsql.db.ext.oracle.OracleDBMeta;

/**
 * Tibero data type
 * 
 * @author ytkim
 *
 */
@MetaBeanConfig(dbVenderType = DBVenderType.TIBERO, metaBean = OracleDBMeta.class, ddlBean = TiberoDDLScript.class, dataTypeBean = TiberoDataTypeFactory.class)
public class TiberoDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public TiberoDataTypeFactory() {
		
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP WITH LOCAL TIME ZONE").typeCode(DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP WITH TIME ZONE").typeCode(DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("CLOB").typeCode(DefaultDataType.CLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("RAW").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("VARCHAR2").typeCode(DefaultDataType.VARCHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("NUMBER").typeCode(DefaultDataType.DECIMAL.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.BIGDECIMAL).build()));
		
	}
}
