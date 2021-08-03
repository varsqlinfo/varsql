package com.varsql.core.db.meta.datatype;

import java.util.HashMap;

import com.varsql.core.db.VarSqlDbDataType;
import com.varsql.core.db.valueobject.DataTypeInfo;

public abstract class DataTypeImpl implements DataType{

	private HashMap<String , DataTypeInfo> dataType = new HashMap<String , DataTypeInfo>(){
		private static final long serialVersionUID = 1L;

	{
		/**
		 * 숫자형.
		 */
		put("-7", new  DataTypeInfo("BIT", true , false,false , VarSqlDbDataType.NUMBER));
		put("BIT", get("-7"));

		put("-6", new  DataTypeInfo("TINYINT", true , false,false , VarSqlDbDataType.NUMBER));
		put("TINYINT", get("-6"));

		put("-5", new  DataTypeInfo("BIGINT", true , false,false , VarSqlDbDataType.NUMBER));
		put("BIGINT", get("-5"));

		put("5", new  DataTypeInfo("SMALLINT", true , false,false , VarSqlDbDataType.NUMBER));
		put("SMALLINT", get("5"));
		put("SHORT", get("5"));

		put("6", new  DataTypeInfo("FLOAT", true , true ,true , VarSqlDbDataType.NUMBER));
		put("FLOAT", get("6"));

		put("7", new  DataTypeInfo("REAL", true , false,false , VarSqlDbDataType.NUMBER));
		put("REAL", get("7"));

		put("8", new  DataTypeInfo("DOUBLE", true , false , false , VarSqlDbDataType.NUMBER));
		put("DOUBLE", get("8"));

		put("2", new  DataTypeInfo("NUMERIC", true , true , true , VarSqlDbDataType.NUMBER));
		put("NUMERIC", get("2"));

		put("3", new  DataTypeInfo("DECIMAL", true , true, true, VarSqlDbDataType.NUMBER));
		put("DECIMAL", get("3"));

		put("4", new  DataTypeInfo("INTEGER", true , true,true , VarSqlDbDataType.NUMBER));
		put("INTEGER", get("4"));
		put("INT", get("4"));

		/**
		 * string 형
		 */
		put("1", new  DataTypeInfo("CHAR", false , false ,true, VarSqlDbDataType.STRING));
		put("CHAR", get("1"));

		put("12", new DataTypeInfo("VARCHAR", false , false ,true, VarSqlDbDataType.STRING));
		put("VARCHAR", get("12"));
		put("VARCHAR2", new DataTypeInfo("VARCHAR2", false , false ,true, VarSqlDbDataType.STRING));

		put("-1", new  DataTypeInfo("LONGVARCHAR", false , false ,true, VarSqlDbDataType.STRING));
		put("LONGVARCHAR", get("-1"));

		put("-15", new  DataTypeInfo("NCHAR", false , false , true ,VarSqlDbDataType.STRING));
		put("NCHAR", get("-15"));

		put("-9", new  DataTypeInfo("NVARCHAR", false , false , true ,VarSqlDbDataType.STRING));
		put("NVARCHAR", get("-9"));

		put("-16", new  DataTypeInfo("LONGNVARCHAR", false , false , true ,VarSqlDbDataType.STRING));
		put("LONGNVARCHAR", get("-16"));
		put("1111", new  DataTypeInfo("NVARCHAR2", false , false , true,VarSqlDbDataType.STRING));
		put("NVARCHAR2", get("1111"));


		/**
		 * date 형
		 */
		put("91", new  DataTypeInfo("DATE", false , false,false,VarSqlDbDataType.DATE));
		put("DATE", get("91"));

		put("92", new  DataTypeInfo("TIME", false , false,false,VarSqlDbDataType.DATE));
		put("TIME", get("92"));

		put("93", new  DataTypeInfo("TIMESTAMP", false , false,false,VarSqlDbDataType.DATE));
		put("TIMESTAMP", get("93"));

		/**
		 * binary 형
		 */
		put("-2", new  DataTypeInfo("BINARY", false , false,true,VarSqlDbDataType.BINARY));
		put("BINARY", get("-2"));

		put("-3", new  DataTypeInfo("VARBINARY", false , false , true,VarSqlDbDataType.BINARY));
		put("VARBINARY", get("-3"));

		put("-4", new  DataTypeInfo("LONGVARBINARY", false , false , true,VarSqlDbDataType.BINARY));
		put("LONGVARBINARY", get("-4"));

		put("2004", new  DataTypeInfo("BLOB", false , false , true,VarSqlDbDataType.BINARY));
		put("BLOB", get("2004"));

		put("2005", new  DataTypeInfo("CLOB", false , false , true,VarSqlDbDataType.BINARY));
		put("CLOB", get("2005"));

		put("-8", new  DataTypeInfo("ROWID", false , false , false,VarSqlDbDataType.BINARY));
		put("ROWID", get("-8"));

		put("2011", new  DataTypeInfo("NCLOB", false , false , true,VarSqlDbDataType.BINARY));
		put("NCLOB", get("2011"));

		put("2009", new  DataTypeInfo("SQLXML", false , false , false,VarSqlDbDataType.BINARY));
		put("SQLXML", get("2009"));

		/**
		 * 기타
		 */
		put("0", new  DataTypeInfo("NULL", false , false , false,VarSqlDbDataType.OTHER));
		put("NULL", get("0"));

		put("2000", new  DataTypeInfo("JAVA_OBJECT", false , false , false,VarSqlDbDataType.OTHER));
		put("JAVA_OBJECT", get("2000"));

		put("2001", new  DataTypeInfo("DISTINCT", false , false , false,VarSqlDbDataType.OTHER));
		put("DISTINCT", get("2001"));

		put("2002", new  DataTypeInfo("STRUCT", false , false , false,VarSqlDbDataType.OTHER));
		put("STRUCT", get("2002"));

		put("2003", new  DataTypeInfo("ARRAY", false , false , false,VarSqlDbDataType.OTHER));
		put("ARRAY", get("2003"));

		put("2006", new  DataTypeInfo("REF", false , false , true,VarSqlDbDataType.OTHER));
		put("REF", get("2006"));

		put("70", new  DataTypeInfo("DATALINK", false , false , true,VarSqlDbDataType.OTHER));
		put("DATALINK", get("70"));

		put("16", new  DataTypeInfo("BOOLEAN", false , false , false,VarSqlDbDataType.OTHER));
		put("BOOLEAN", get("16"));

		put("9999", new  DataTypeInfo("OTHER", false , false , false,VarSqlDbDataType.OTHER));
		put("OTHER", get("9999"));

		put("STRING", new DataTypeInfo("VARCHAR", false , false ,true, VarSqlDbDataType.STRING));

	}};

	protected DataTypeImpl(){}

	public DataTypeInfo getDataType(String dataType) {
		dataType=getUpperCase(dataType);
		return this.dataType.containsKey(dataType) ?  this.dataType.get(dataType) : this.dataType.get("9999");
	}

	public boolean isDataType (String dataType){
		dataType=getUpperCase(dataType);
		return this.dataType.containsKey(dataType);
	}

	public String getDataTypeName(String dataType) {
		dataType=getUpperCase(dataType);
		return isDataType(dataType)?this.dataType.get(dataType).getDataTypeName():null;
	}

	public boolean isNum(String dataType) {
		dataType=getUpperCase(dataType);
		return isDataType(dataType)?this.dataType.get(dataType).isNum():false;
	}

	public boolean isRange(String dataType) {
		dataType=getUpperCase(dataType);
		return isDataType(dataType)?this.dataType.get(dataType).isRange():false;
	}

	public boolean isSize(String dataType) {
		dataType=getUpperCase(dataType);
		return isDataType(dataType)?this.dataType.get(dataType).isSizeYn():false;
	}

	public void setDataTypeInfo(String dataType, DataTypeInfo dataTypeInfo) {
		dataType=getUpperCase(dataType);

		if(isDataType(dataType)){
			getDataType(dataType).setDataTypeInfo(dataTypeInfo);
		}else{
			this.dataType.put(dataType, dataTypeInfo);
		}
	}

	private String getUpperCase(String str){
		return str.toUpperCase();
	}

}
