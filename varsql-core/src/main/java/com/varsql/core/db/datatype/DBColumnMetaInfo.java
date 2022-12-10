package com.varsql.core.db.datatype;

import com.vartech.common.utils.StringUtils;

/**
 * 
*-----------------------------------------------------------------------------
* @fileName	: DBColumnMetaInfo.java
* @desc		: db data type의 메타 정보 처리에 필요한 클래스 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2022. 3. 25. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public enum DBColumnMetaInfo {
	
	BIT(0, false)
	,TINYINT(1, false)
	,SMALLINT(1, false)
	,INT(1, false)
	,BIGINT(1, false)
	,INTEGER(1,true)
	,FLOAT(1, true, new ColumnMetaHandler(){
		@Override
		public String typeAndLength(int precision, int scale) {
			if(precision < 0) return "";
			return "(" + precision+ (scale> 0 ? "," + scale :"") +")";
		}
	})
	,DOUBLE(1, true, new ColumnMetaHandler(){
		@Override
		public String typeAndLength(int precision, int scale) {
			if(precision < 0) return "";
			return "(" + precision+ (scale> 0 ? "," + scale :"") +")";
		}
	})
	,NUMERIC(1, true, new ColumnMetaHandler(){
		@Override
		public String typeAndLength(int precision, int scale) {
			if(precision < 0) return "";
			return "(" + precision+ (scale> 0 ? "," + scale :"") +")";
		}
	})
	,BIGDECIMAL(1, true, new ColumnMetaHandler(){
		@Override
		public String typeAndLength(int precision, int scale) {
			if(precision < 0) return "";
			return "(" + precision+ (scale> 0 ? "," + scale :"") +")";
		}
	})
	,DECIMAL(1, true, new ColumnMetaHandler(){
		@Override
		public String typeAndLength(int precision, int scale) {
			if(precision < 0) return "";
			return "(" + precision+ (scale> 0 ? "," + scale :"") +")";
		}
	})
	,STRING(2, true)
	,DATE(3, false)
	,TIME(3, false)
	,TIMESTAMP(3, false)
	,CLOB(5, true)
	,BINARY(6, true)
	,ARRAY(7, true)
	,BLOB(8, true)
	,TEXT(9, false)
	,SQLXML(10, false)
	,OTHER(99, false);
	
	
	private int typeNum;
	private boolean size;
	private ColumnMetaHandler columnMetaHandler;
	
	DBColumnMetaInfo(int typeNum, boolean size){
		this(typeNum, size, null);
	}
	
	DBColumnMetaInfo(int typeNum, boolean size, ColumnMetaHandler columnMetaHandler){
		this.typeNum = typeNum;
		this.size = size;
		this.columnMetaHandler = columnMetaHandler; 
	}
	
	public boolean isNumber() {
		return this.typeNum ==1;
	}
	
	public boolean isString() {
		return this.typeNum ==2;
	}
	
	public boolean isDate() {
		return this.typeNum ==3;
	}
	
	public boolean isClob() {
		return this.typeNum ==5;
	}
	
	public boolean isBinary() {
		return this.typeNum ==6;
	}
	
	public boolean isBlob() {
		return this.typeNum ==8;
	}
	
	public boolean isSize() {
		return size;
	}
	
	public String getTypeAndLength(String dataType, DataType dataTypeInfo, String typeAndLength, long columnSize, int precision, int scale) {
		if(!StringUtils.isBlank(typeAndLength)) {
			return typeAndLength;
		}else {
			columnSize = dataTypeInfo.getMetaDataHandler().getColumnLength(columnSize);
			
			if(this.columnMetaHandler != null) {
				dataType += this.columnMetaHandler.typeAndLength(precision, scale);
			}else {
				
				if(columnSize < 0) return dataType;
				
				if(this.isSize()){
					dataType +="(" + columnSize+")";
				}
			}
			
			return dataType;
		}
	}
	
	interface ColumnMetaHandler{
		String typeAndLength(int precision, int scale);
	}
}
