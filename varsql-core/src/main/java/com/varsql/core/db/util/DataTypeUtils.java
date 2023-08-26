package com.varsql.core.db.util;

import java.util.regex.Pattern;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DefaultDataType;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.vartech.common.utils.StringUtils;

public final class DataTypeUtils {
	
	private DataTypeUtils() {}

	public static DataType getDataType(String typeName,  DBVenderType dbType, ColumnInfo item) {
		return getDataType(typeName, dbType.name(), item);
	}
	
	public static DataType getDataType(String typeName, String dbType , ColumnInfo item) {
		DataType dataTypeInfo = null;
		
		if(StringUtils.isBlank(typeName)) {
			dataTypeInfo = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl().getDataType(item.getTypeCode(), null);
		}else {
			String standardTypeName = DbMetaUtils.getTypeName(item.getTypeName());
			
			if(!StringUtils.isBlank(standardTypeName)) {
				dataTypeInfo = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl().getDataType(0, standardTypeName);
			}
			
			if((dataTypeInfo == null || DefaultDataType.OTHER.equals(dataTypeInfo)) && !StringUtils.isBlank(item.getTypeAndLength())) {
				dataTypeInfo = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl().getDataType(0, DbMetaUtils.getTypeName(item.getTypeAndLength()));
			}
		}
		
		return dataTypeInfo; 
	}
	
	
	public static boolean isTypeAndLengthPattern(String typeName) {
		return Pattern.matches(".+\\(.+\\).*", typeName);
	}

	
}
