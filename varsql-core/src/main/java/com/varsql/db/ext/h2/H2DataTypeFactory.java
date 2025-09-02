package com.varsql.db.ext.h2;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;
import com.varsql.core.db.meta.MetaBeanConfig;

/**
 * H2D dataType Factory
 * 
 * @author ytkim
 *
 */
@MetaBeanConfig(dbVenderType = DBVenderType.H2, metaBean = H2DBMeta.class, ddlBean = H2DDLScript.class, dataTypeBean = H2DataTypeFactory.class)
public class H2DataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public H2DataTypeFactory() {
		
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("VARCHAR_IGNORECASE").typeCode(DefaultDataType.NVARCHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("CHARACTER VARYING").typeCode(DefaultDataType.VARCHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("CHARACTER LARGE OBJECT").typeCode(DefaultDataType.CLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("CHARACTER").typeCode(DefaultDataType.CHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIME WITH TIME ZONE").typeCode(DefaultDataType.TIME.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIME).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP WITH TIME ZONE").typeCode(DefaultDataType.TIMESTAMP.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.DATE).build()));
		
	}
}
