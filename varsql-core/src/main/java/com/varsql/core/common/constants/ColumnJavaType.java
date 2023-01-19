package com.varsql.core.common.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColumnJavaType{
	DECIMAL(true,false)
	, FLOAT(true,false)
	, DOUBLE(true,false)
	, NUMBER(true,false)
	, INTEGER(true,false)
	, DATE(false,false)
	, TIME(false,false)
	, TIMESTAMP(false,false)
	, BLOB(false,true)
	, CLOB(false,true)
	, REF(false,true)
	, NCLOB(false,true)
	, BINARY(false,true)
	, SQLXML(false,true)
	, ARRAY(false,false)
	, STRING(false,false);
	
	private boolean number;
	private boolean lob;
	private String type;
	
	ColumnJavaType(boolean number, boolean lob) {
		this.number = number; 
		this.lob = lob; 
		this.type = name().toLowerCase();
	}

	public boolean isNumber() {
		return number;
	}

	public boolean isLob() {
		return lob;
	}
	
	@JsonValue
	public String getType() {
		return type;
	}
	
	public static ColumnJavaType getType(String type) {
		for(ColumnJavaType columnJavaType : ColumnJavaType.values()) {
			if(columnJavaType.name().equalsIgnoreCase(type)) {
				return columnJavaType;
			}			
		}
		return ColumnJavaType.STRING;
	}
	
	
}