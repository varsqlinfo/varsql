package com.varsql.core.db.valueobject;

public enum VarSqlDbType {
	
	NUMBER(1)
	,STRING(2)
	,DATE(3)
	,BINARY(4)
	,OTHER(99);
	
	private int typeNum;
	
	VarSqlDbType(int typeNum){
		this.typeNum = typeNum;
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
	
		
}
