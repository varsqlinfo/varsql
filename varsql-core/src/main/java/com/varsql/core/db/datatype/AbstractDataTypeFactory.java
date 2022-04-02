package com.varsql.core.db.datatype;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractDataTypeFactory implements DataTypeFactory {
	protected AbstractDataTypeFactory() {};
	
	private ConcurrentMap<String , DataType> venderDataType = new ConcurrentHashMap<String , DataType>();
	
	public DataType getDataType(String typeName) {
		if(typeName ==null) return DefaultDataType.OTHER;
		
		typeName = typeName.toUpperCase();
		
		if(venderDataType.containsKey(typeName)) {
			return venderDataType.get(typeName);
		}
		
		return DefaultDataType.getDataType(typeName);
	}
	
	public void addDataType(DataType dataType) {
		String typeName = dataType.getTypeName().toUpperCase(); 
		if(!venderDataType.containsKey(typeName)) {
			venderDataType.put(typeName, dataType);
		}
	};
}
