package com.varsql.web.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.encryption.EncryptionFactory;
import com.vartech.common.encryption.EncryptDecryptException;

@Converter
public class DbPasswordEncodeConverter implements AttributeConverter<String, String> {
	private static final Logger logger = LoggerFactory.getLogger(DbPasswordEncodeConverter.class);
	
	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			return EncryptionFactory.getInstance().encrypt(attribute);
		} catch (EncryptDecryptException e) {
			logger.error("DbConnPwEncodeConverter : {} " , e.getMessage() , e);
		}
		return null; 
	}
	
	@Override
	public String convertToEntityAttribute(String s) {
		
		if(s==null || "".equals(s)) return null; 
		
		try {
			return EncryptionFactory.getInstance().decrypt(s);
		} catch (EncryptDecryptException e) {
			logger.error("DbConnPwEncodeConverter : {} " , e.getMessage() , e);
		}
		return s; 
	}
	
}