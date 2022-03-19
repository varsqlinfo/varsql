package com.varsql.core.db.report;

import java.util.HashMap;
import java.util.Map;

import com.vartech.common.app.beans.EnumMapperType;
import com.vartech.common.report.ExcelConstants;

public interface VarsqlReportConfig {
	
	enum MapperKey{
		code("code"), title("title"),width("width") , custom("custom");
		
		private String key;
		MapperKey(String key){
			this.key = key ; 
		}
		
		public String getKey() {
			return key;
		}
	}
	
	enum TABLE_COLUMN implements EnumMapperType{
		NO("no", "NO", 2 ), 	// no 
		NAME("name", "NAME", 15),	// 컬럼 명.
		DATATYPE("dataType", "DATATYPE", 6), // 컬럼 타입
		TYPENAME("typeName", "TYPE", 10),	// 컬럼 타입명
		TYPEANDLENGTH("typeAndLength", "Type and length", 13),	// 컬럼 타입명
		LENGTH("length", "Length", 6),	//사이즈
		NULLABLE("nullable", "Nullable", 6, ExcelConstants.ALIGN.CENTER.align()),	// null 여부.
		COMMENT("comment", "Comment", 15), 	// 코멘트
		CONSTRAINTS("constraints", "constraints", 6, ExcelConstants.ALIGN.CENTER.align()),	// 제약조건 여부.
		AUTOABLE("autoincrement", "Autoable", 8), // 자동증가 여부.
		DEFAULTVAL("defaultVal", "DEFAULTVAL", 10); 	// default value
		
		private String key; 
		private String name; 
		private int width;
		private ExcelConstants.ALIGN align;
		private Map<String , Object> custom = new HashMap<String , Object> ();
		
		TABLE_COLUMN(String key ,String name , int width){
			this(key , name , width , ExcelConstants.ALIGN.LEFT.align());
		}
		
		TABLE_COLUMN(String key ,String name , int width , int align){
			this.key = key; 
			this.name= name; 
			this.width = width;
			this.align = ExcelConstants.ALIGN.getAlign(align);
			
			custom.put("width", width);
		}
		
		public String colName(){
			return this.name;
		}
		
		public int colWidth(){
			return this.width;
		}
		
		public ExcelConstants.ALIGN getAlign(){
			return this.align;
		}

		public String getKey() {
			return key;
		}

		public String getCode() {
			return this.name();
		}
		
		public String getTitle() {
			return name;
		}
		
		@Override
		public Map<String, Object> getCustom() {
			return custom;
		}
	}
	
	enum TABLE implements EnumMapperType{
		DATABASE("database", "DATABASE",6),	// database 명.
		NAME("name", "NAME",20),	// table name.
		USER("user", "USER",6),	// table user.
		COMMENT("comment","Comment",8); 	// 코멘트
		
		private String key; 
		private String label; 
		private int width;
		private Map<String , Object> custom = new HashMap<String , Object> ();
		
		TABLE(String key ,String name , int width){
			this.key = key; 
			this.label= name; 
			this.width = width;
			
			custom.put("width", width);
		}
		
		public String getLabel(){
			return this.label;
		}
		
		public int getWidth(){
			return this.width;
		}

		public String getKey() {
			return key;
		}

		@Override
		public String getCode() {
			return this.name();
		}

		@Override
		public String getTitle() {
			return label;
		}
		
		@Override
		public Map<String, Object> getCustom() {
			return custom;
		}
	}
}
