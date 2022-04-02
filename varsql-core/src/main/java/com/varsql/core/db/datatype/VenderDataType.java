package com.varsql.core.db.datatype;

import org.apache.commons.lang3.Validate;

import com.varsql.core.db.datatype.handler.MetaDataHandler;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.datatype.handler.StatementHandler;

public class VenderDataType implements DataType{
	
	@SuppressWarnings("unused")
	private VenderDataType(){};
	
	private String typeName;
	private int typeCode;
	private DBColumnMetaInfo jdbcDataTypeMetaInfo;
	private DataTypeHandler dataTypeHandler;
	
	public VenderDataType(String typeName, int typeCode, DBColumnMetaInfo jdbcDataTypeMetaInfo){
		this(typeName, typeCode, jdbcDataTypeMetaInfo, null);
	};
	public VenderDataType(String typeName, int typeCode, DBColumnMetaInfo jdbcDataTypeMetaInfo, DataTypeHandler dataTypeHandler){
		Validate.notNull(typeName, "typeName can't be null");
		
		this.typeName = typeName; 
		this.typeCode = typeCode; 
		this.jdbcDataTypeMetaInfo = jdbcDataTypeMetaInfo;
		this.dataTypeHandler = dataTypeHandler != null ? dataTypeHandler :DataTypeHandler.builder().build();
	};

	@Override
	public int getTypeCode() {
		return this.typeCode;
	}

	@Override
	public String getTypeName() {
		return this.typeName;
	}

	@Override
	public DBColumnMetaInfo getJDBCDataTypeMetaInfo() {
		return this.jdbcDataTypeMetaInfo;
	}

	@Override
	public StatementHandler getStatementHandler() {
		return this.dataTypeHandler.getStatementHandler();
	}

	@Override
	public ResultSetHandler getResultSetHandler() {
		return this.dataTypeHandler.getResultSetHandler();
	}
	
	@Override
	public MetaDataHandler getMetaDataHandler() {
		return this.dataTypeHandler.getMetaDataHandler();
	}
	
	public static VenderDataType newCustomDataType(String typeName, DefaultDataType dataType) {
		return new VenderDataType(typeName, dataType.getTypeCode(), dataType.getJDBCDataTypeMetaInfo(), dataType.getDataTypeHandler());
	}
	
}
