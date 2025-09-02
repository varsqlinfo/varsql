package com.varsql.db.ext.postgresql;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DataTypeConfigInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;
import com.varsql.core.db.meta.MetaBeanConfig;
import com.varsql.db.ext.oracle.OracleDBMeta;

/**
 * Postgresql data type
 * 
 * @author ytkim
 *
 */
@MetaBeanConfig(dbVenderType = DBVenderType.POSTGRESQL, metaBean = OracleDBMeta.class, ddlBean = PostgresqlDDLScript.class, dataTypeBean = PostgresqlDataTypeFactory.class)
public class PostgresqlDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public PostgresqlDataTypeFactory() {
		
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("character varying").typeCode(DefaultDataType.VARCHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("character").typeCode(DefaultDataType.CHAR.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.STRING).build()));
		//addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("double precision").typeCode(DefaultDataType.FLOAT.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.FLOAT).build()));
		//addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("bigserial").typeCode(DefaultDataType.INTEGER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.NUMERIC).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("box").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("bytea").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("cidr").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("circle").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("inet").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("interval").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("line").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("lseg").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("macaddr").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("money").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("path").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("point").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("polygon").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("serial").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("tsquery").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("tsvector").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("txid_snapshot").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("uuid").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.OTHER).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("text").typeCode(DefaultDataType.CLOB.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("timestamp without time zone").typeCode(DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TIMESTAMP).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("json").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		addDataType(new VenderDataType(DataTypeConfigInfo.builder().typeName("jsonb").typeCode(DefaultDataType.OTHER.getTypeCode()).jdbcDataTypeMetaInfo(DBColumnMetaInfo.TEXT).build()));
		
	}
}
