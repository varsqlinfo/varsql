package com.varsql.core.db.datatype;

public interface DataTypeFactory {
	public DataType getDataType(int typeCode, String typeName);
}
