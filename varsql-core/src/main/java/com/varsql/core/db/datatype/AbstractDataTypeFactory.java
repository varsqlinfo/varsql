package com.varsql.core.db.datatype;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.vartech.common.utils.StringUtils;

public abstract class AbstractDataTypeFactory implements DataTypeFactory {
	public static final int VARCHAR_DEFAULT_SIZE = 512;
	
	private int typeCodeSeq = 1000000;
	
	protected AbstractDataTypeFactory() {};
	
	private ConcurrentMap<Integer , DataType> typeCodeDataType = new ConcurrentHashMap<Integer, DataType>();
	
	private DataType getDataType(String typeName) {
		
		if(StringUtils.isBlank(typeName)) return DefaultDataType.OTHER;
		
		typeName = typeName.toUpperCase();
		
		for(DataType item: typeCodeDataType.values()) {
			if(item.getTypeName().equals(typeName)) {
				return item;
			}
		}
		return DefaultDataType.getDataType(typeName);
	}
	
	private DataType getDataType(int typeCode) {
		
		if(typeCodeDataType.containsKey(typeCode)) {
			return typeCodeDataType.get(typeCode);
		}
				
		if(typeCode == 0) {
			return null;
		}
		
		return DefaultDataType.getDataType(typeCode);
	}
	
	public DataType getDataType(int typeCode, String typeName) {
		
		DataType typeNameDataType = null; 
		if(typeCode == 0 && !StringUtils.isBlank(typeName)) {
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
		int typeCode =dataType.getTypeCode(); 
		if(typeCodeDataType.containsKey(typeCode)) {
			typeCodeSeq += 1000000;
			if(typeCode < 0) { // 타입 코드가 minus 인경우  이걸 구분하기 위해서 90000 으로 값을 넣고 typecode를 양수로 변경한다.  
				typeCodeDataType.put(typeCodeSeq + 90000 + Math.abs(typeCode), dataType);
			}else {
				typeCodeDataType.put(typeCodeSeq + typeCode, dataType);
			}
		}else {
			typeCodeDataType.put(dataType.getTypeCode(), dataType);
		}
		
	};
}
