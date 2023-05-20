package com.varsql.core.db.datatype;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractDataTypeFactory implements DataTypeFactory {
	public static final int VARCHAR_DEFAULT_SIZE = 512;
	
	protected AbstractDataTypeFactory() {};
	
	private ConcurrentMap<String , DataType> venderDataType = new ConcurrentHashMap<String, DataType>();
	
	public DataType getDataType(String typeName) {
		if(typeName ==null) return DefaultDataType.OTHER;
		
		typeName = typeName.toUpperCase();
		
		if(venderDataType.containsKey(typeName)) {
			return venderDataType.get(typeName);
		}
		
		return DefaultDataType.getDataType(typeName);
	}
	
	public DataType getDataType(int typeCode) {
		DataType dataType =  venderDataType.values().stream().filter(item-> item.getTypeCode() == typeCode).findFirst().orElse(DefaultDataType.getDataType(typeCode));
		
		return dataType != null ? dataType :  DefaultDataType.OTHER;
	}
	
	public void addDataType(DataType dataType) {
		venderDataType.put(dataType.getTypeName().toUpperCase(), dataType);
	};
}
