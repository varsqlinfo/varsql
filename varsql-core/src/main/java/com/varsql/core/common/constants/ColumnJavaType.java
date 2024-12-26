package com.varsql.core.common.constants;

import java.sql.Types;

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
	
	
	public static ColumnJavaType getType(int columnType) {
		if(columnType ==Types.NUMERIC || columnType ==Types.DECIMAL){
			return ColumnJavaType.DECIMAL;
		}
		
		if(columnType ==Types.REAL || columnType == Types.FLOAT) {
			return ColumnJavaType.FLOAT;
		}
		
		if(columnType ==Types.DOUBLE) {
			return ColumnJavaType.DOUBLE;
		}
		
		if(columnType == Types.INTEGER||columnType ==Types.BIGINT
				||columnType ==Types.SMALLINT||columnType ==Types.TINYINT){
			return ColumnJavaType.INTEGER;
		}
		
		if(columnType == Types.DATE ){
			return ColumnJavaType.DATE;
		}
		
		if(columnType == Types.TIME ){
			return ColumnJavaType.TIME;
		}
		
		if(columnType == Types.TIMESTAMP ){
			return ColumnJavaType.TIMESTAMP;
		}
		
		if(columnType == Types.BLOB ){
			return ColumnJavaType.BLOB;
		}
		
		if(columnType == Types.CLOB ){
			return ColumnJavaType.CLOB;
		} 
		
		if(columnType == Types.REF ){
			return ColumnJavaType.REF;
		}
		
		if(columnType == Types.NCLOB ){
			return ColumnJavaType.NCLOB;
		}
		
		if(columnType == Types.VARBINARY ||columnType == Types.BINARY || columnType == Types.LONGVARBINARY){
			return ColumnJavaType.BINARY;
		}
		
		if(columnType == Types.SQLXML ){
			return ColumnJavaType.SQLXML;
		}
		
		if(columnType == Types.ARRAY){
			return ColumnJavaType.ARRAY;
		}
		
		return ColumnJavaType.STRING;
	}
}