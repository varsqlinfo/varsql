package com.varsql.core.db.datatype;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.vartech.common.utils.StringUtils;

public abstract class AbstractDataTypeFactory implements DataTypeFactory {
	public static final int VARCHAR_DEFAULT_SIZE = 512;
	
	protected AbstractDataTypeFactory() {};
	
	private ConcurrentMap<String , DataType> venderDataType = new ConcurrentHashMap<String, DataType>();
	
	private DataType getDataType(String typeName) {
		
		if(StringUtils.isBlank(typeName)) return DefaultDataType.OTHER;
		
		typeName = typeName.toUpperCase();
		
		for(DataType item: venderDataType.values()) {
			if(item.getTypeName().equals(typeName)) {
				return item;
			}
		}
		return DefaultDataType.getDataType(typeName);
	}
	
	private DataType getDataType(int typeCode) {
		
		for(DataType item: venderDataType.values()) {
			if(item.getTypeCode() == typeCode) {
				return item;
			}
		}
		
		if(typeCode == 0) {
			return null;
		}
		
		return DefaultDataType.getDataType(typeCode);
	}
	
	public DataType getDataType(int typeCode, String typeName) {
		
		DataType typeNameDataType = null; 
		if(typeCode != 0 && !StringUtils.isBlank(typeName)) {
			typeNameDataType = getDataType(typeName);
			if(typeNameDataType.getTypeCode() == typeCode) {
				return typeNameDataType;
			}
		}
		
		DataType dataType = getDataType(typeCode);
		
		if(dataType != null && !DefaultDataType.OTHER.equals(dataType)) {
			return dataType;
		}
		
		return typeNameDataType != null ? typeNameDataType : getDataType(typeName);
	}
	
	public void addDataType(DataType dataType) {
		venderDataType.put(dataType.getTypeName().toUpperCase(), dataType);
	};
}
