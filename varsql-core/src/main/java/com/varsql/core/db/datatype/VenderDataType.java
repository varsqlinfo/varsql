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
	private boolean enableDefaultTypeName; // import 시 제외할 컬럼 정보
	private String defaultTypeName;
	
	
	public VenderDataType(DataTypeConfigInfo dataTypeConfigInfo){
		
		Validate.notNull(dataTypeConfigInfo, "DataTypeConfigInfo can't be null");
		Validate.notNull(dataTypeConfigInfo.getTypeName(), "typeName can't be null");
		
		this.typeName = dataTypeConfigInfo.getTypeName().toUpperCase(); 
		this.typeCode = dataTypeConfigInfo.getTypeCode(); 
		this.jdbcDataTypeMetaInfo =  dataTypeConfigInfo.getJdbcDataTypeMetaInfo();
		this.excludeImportColumn = dataTypeConfigInfo.isExcludeImportColumn();
		this.enableDefaultTypeName = dataTypeConfigInfo.isEnableDefaultTypeName();
		
		DefaultDataType ddt = DefaultDataType.getDefaultDataType(typeCode);
		
		this.defaultTypeName = ddt.getTypeName();
		if(dataTypeConfigInfo.getDefaultSize() > 0) {
			this.defaultSize = dataTypeConfigInfo.getDefaultSize();
		}else {
			this.defaultSize = ddt.getDefaultSize();
		}
		
		if(dataTypeConfigInfo.getDataTypeHandler() != null) {
			this.dataTypeHandler = dataTypeConfigInfo.getDataTypeHandler();
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
	
	public static VenderDataType newCustomDataType(String typeName, DefaultDataType dataType) {
		return newCustomDataType(typeName, dataType, -1);
	}
	
	public static VenderDataType newCustomDataType(String typeName, DefaultDataType dataType, int defaultSize) {
		return new VenderDataType(DataTypeConfigInfo.builder()
			.typeName(typeName)
			.typeCode(dataType.getTypeCode())
			.jdbcDataTypeMetaInfo(dataType.getJDBCDataTypeMetaInfo())
			.defaultSize(defaultSize)
			.dataTypeHandler(dataType.getDataTypeHandler())
			.excludeImportColumn(dataType.isExcludeImportColumn())
			.build());
	}
	
	@Override
	public boolean isExcludeImportColumn() {
		return this.excludeImportColumn;
	}

	@Override
	public int getDefaultSize() {
		return this.defaultSize;
	}

	@Override
	public String getViewTypeName(String typeName) {
		return this.enableDefaultTypeName ? this.defaultTypeName :typeName;
	}
}
