package com.varsql.core.db.datatype;

import org.apache.commons.lang3.Validate;

import com.varsql.core.db.datatype.handler.MetaDataHandler;
import com.varsql.core.db.datatype.handler.ResultSetHandler;
import com.varsql.core.db.datatype.handler.StatementHandler;

/**
 * db vender data type 
* 
* @fileName	: VenderDataType.java
* @author	: ytkim
 */
public class VenderDataType implements DataType{
	
	@SuppressWarnings("unused")
	private VenderDataType(){};
	
	private String typeName;
	private int typeCode;
	private int defaultSize;
	private DBColumnMetaInfo jdbcDataTypeMetaInfo;
	private DataTypeHandler dataTypeHandler;
	private boolean excludeImportColumn; // import 시 제외할 컬럼 정보
	
	public VenderDataType(String typeName, int typeCode, DBColumnMetaInfo jdbcDataTypeMetaInfo){
		this(typeName, typeCode, jdbcDataTypeMetaInfo, -1, null);
	};
	
	public VenderDataType(String typeName, int typeCode, DBColumnMetaInfo jdbcDataTypeMetaInfo, int defaultSize){
		this(typeName, typeCode, jdbcDataTypeMetaInfo, defaultSize, null);
	};
	
	public VenderDataType(String typeName, int typeCode, DBColumnMetaInfo jdbcDataTypeMetaInfo, int defaultSize, DataTypeHandler dataTypeHandler){
		this(typeName, typeCode, jdbcDataTypeMetaInfo, defaultSize, null, false);
	}
	
	public VenderDataType(String typeName, int typeCode, DBColumnMetaInfo jdbcDataTypeMetaInfo, int defaultSize, DataTypeHandler dataTypeHandler, boolean excludeImportColumn){
		Validate.notNull(typeName, "typeName can't be null");
		
		this.typeName = typeName.toUpperCase(); 
		this.typeCode = typeCode; 
		this.jdbcDataTypeMetaInfo = jdbcDataTypeMetaInfo;
		this.excludeImportColumn = excludeImportColumn;
		
		DefaultDataType ddt = DefaultDataType.getDefaultDataType(typeCode);
		
		if(defaultSize > 0) {
			this.defaultSize = defaultSize;
		}else {
			this.defaultSize = ddt.getDefaultSize();
		}
		
		if(dataTypeHandler != null) {
			this.dataTypeHandler = dataTypeHandler;
		}else {
			if(ddt.equals(DefaultDataType.OTHER)) {
				this.dataTypeHandler = DefaultDataType.getDefaultDataType(jdbcDataTypeMetaInfo.name()).getDataTypeHandler();
			}else {
				this.dataTypeHandler = ddt.getDataTypeHandler();
			}
		}
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
	
	public static VenderDataType newCustomDataType(DefaultDataType dataType, int defaultSize) {
		return new VenderDataType(dataType.getTypeName(), dataType.getTypeCode(), dataType.getJDBCDataTypeMetaInfo(), defaultSize, dataType.getDataTypeHandler(), dataType.isExcludeImportColumn());
	}
	
	public static VenderDataType newCustomDataType(String typeName, DefaultDataType dataType) {
		return new VenderDataType(typeName, dataType.getTypeCode(), dataType.getJDBCDataTypeMetaInfo(), -1, dataType.getDataTypeHandler(), dataType.isExcludeImportColumn());
	}
	
	public static VenderDataType newCustomDataType(String typeName, DefaultDataType dataType, int defaultSize) {
		return new VenderDataType(typeName, dataType.getTypeCode(), dataType.getJDBCDataTypeMetaInfo(), defaultSize, dataType.getDataTypeHandler(), dataType.isExcludeImportColumn());
	}
	
	@Override
	public boolean isExcludeImportColumn() {
		return this.excludeImportColumn;
	}

	@Override
	public int getDefaultSize() {
		return this.defaultSize;
	}
}
