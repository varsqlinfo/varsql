package com.varsql.db.ext.postgresql;

import com.varsql.core.db.datatype.AbstractDataTypeFactory;
import com.varsql.core.db.datatype.DBColumnMetaInfo;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.datatype.VenderDataType;

/**
 * 
 * @FileName  : PostgresqlDataTypeFactory.java
 * @프로그램 설명 : Postgresql data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class PostgresqlDataTypeFactory extends AbstractDataTypeFactory{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public PostgresqlDataTypeFactory() {
		addDataType(new VenderDataType("character varying", DefaultDataType.VARCHAR.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("character", DefaultDataType.CHAR.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("double precision", DefaultDataType.DECIMAL.getTypeCode() , DBColumnMetaInfo.DECIMAL));
		addDataType(new VenderDataType("bigserial", DefaultDataType.INTEGER.getTypeCode() , DBColumnMetaInfo.NUMERIC));
		addDataType(new VenderDataType("box", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("bytea", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("cidr", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("circle", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("inet", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("interval", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("line", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("lseg", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("macaddr", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("money", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("path", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("point", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("polygon", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("serial", DefaultDataType.INTEGER.getTypeCode() , DBColumnMetaInfo.NUMERIC));
		addDataType(new VenderDataType("tsquery", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("tsvector", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("txid_snapshot", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("uuid", DefaultDataType.OTHER.getTypeCode() , DBColumnMetaInfo.OTHER));
		addDataType(new VenderDataType("text", DefaultDataType.VARCHAR.getTypeCode() , DBColumnMetaInfo.STRING));
		addDataType(new VenderDataType("timestamp without time zone", DefaultDataType.TIMESTAMP_WITH_TIMEZONE.getTypeCode() , DBColumnMetaInfo.DATE));
	}
}
