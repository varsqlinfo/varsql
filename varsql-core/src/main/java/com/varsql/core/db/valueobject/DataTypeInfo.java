package com.varsql.core.db.valueobject;

import com.varsql.core.db.VarSqlDbDataType;

public class DataTypeInfo {
	
	// db type
	private String dataTypeName;
	// 숫자 여부. 
	private boolean num;
	// size 여부.
	private boolean sizeYn;

	// column 범위 여부. 
	private boolean range;
	
	// 문자 , 숫자 , 날짜 , 기타 등등  구분 
	private VarSqlDbDataType varsqlType;
	
	public DataTypeInfo(String dataTypeName, boolean num , boolean range){
		this(dataTypeName, num, range, false);
	}
	public DataTypeInfo(String dataTypeName, boolean num , boolean range, boolean sizeYn){
		this(dataTypeName, num, range, sizeYn, VarSqlDbDataType.OTHER);
	}
	
	public DataTypeInfo(String dataTypeName, boolean num , boolean range, boolean sizeYn, VarSqlDbDataType type){
		this.dataTypeName =dataTypeName;
		this.num =num;
		this.range = range;
		this.sizeYn = sizeYn;
		this.varsqlType = type;
	}
	
	public void setDataTypeInfo(DataTypeInfo dataTypeInfo){
		this.dataTypeName =dataTypeInfo.getDataTypeName();  
		this.num =dataTypeInfo.isNum();
		this.range = dataTypeInfo.isRange();
		this.sizeYn = dataTypeInfo.isSizeYn();
		this.varsqlType = dataTypeInfo.getDbType();
	}

	public boolean isRange() {
		return range;
	}

	public void setRange(boolean range) {
		this.range = range;
	}

	public boolean isNum() {
		return num;
	}

	public void setNum(boolean num) {
		this.num = num;
	}
	
	public boolean isSizeYn() {
		return sizeYn;
	}
	
	public void setSizeYn(boolean sizeYn) {
		this.sizeYn = sizeYn;
	}
	
	public VarSqlDbDataType getDbType() {
		return varsqlType;
	}
	public void setDbType(VarSqlDbDataType dbType) {
		this.varsqlType = dbType;
	}
	
	public boolean  isNumber() {
		return this.varsqlType.isNumber();
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	
}
