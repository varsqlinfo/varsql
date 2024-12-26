package com.varsql.core.db.datatype;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DataTypeConfigInfo {
	
	// data type name
	private String typeName;
	
	// column type code
	private int typeCode;
	
	private DBColumnMetaInfo jdbcDataTypeMetaInfo;
	
	// default size
	@Builder.Default
	private int defaultSize = -1; 
	
	private DataTypeHandler dataTypeHandler;
	
	@Builder.Default
	private boolean excludeImportColumn = false;
	
	@Builder.Default
	private boolean enableDefaultTypeName = false;
}
