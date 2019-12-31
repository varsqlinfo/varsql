package com.varsql.core.configuration.xml;

public interface XmlField {
	
	enum OBJECT_TYPE{
		OBJECT,COLLECTION; 
	}
	
	String getFieldName();
	String[] getFieldAttr();
	OBJECT_TYPE getObjectType();
}
