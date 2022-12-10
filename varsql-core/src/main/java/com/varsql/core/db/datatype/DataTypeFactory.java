package com.varsql.core.db.datatype;

public interface DataTypeFactory {
	public DataType getDataType(String typeName);
	
	public DataType getDataType(int typeCode);
}
