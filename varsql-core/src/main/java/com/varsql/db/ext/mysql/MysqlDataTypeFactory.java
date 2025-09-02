package com.varsql.db.ext.mysql;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;
import com.varsql.core.db.meta.MetaBeanConfig;

/**
 * Mysql data type
 * @author User
 *
 */
@MetaBeanConfig(dbVenderType = DBVenderType.MYSQL, metaBean = MysqlDBMeta.class, ddlBean = MysqlDDLScript.class, dataTypeBean = MysqlDataTypeFactory.class)
public class MysqlDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public MysqlDataTypeFactory() {
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("VARCHAR").typeCode(DefaultDataType.VARCHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).defaultSize(VARCHAR_DEFAULT_SIZE).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TEXT").typeCode(DefaultDataType.CLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("NTEXT").typeCode(DefaultDataType.NCLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIME").typeCode(DefaultDataType.TIME_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("TIMESTAMP").typeCode(DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.DATE).build()));
	}
}
