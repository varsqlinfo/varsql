package com.varsql.web.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.crypto.DBPasswordCryptionFactory;
import com.vartech.common.crypto.EncryptDecryptException;

@Converter
public class DbPasswordEncodeConverter implements AttributeConverter<String, String> {
	private final Logger logger = LoggerFactory.getLogger(DbPasswordEncodeConverter.class);

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if(attribute==null || "".equals(attribute)) return null;

		try {
			return DBPasswordCryptionFactory.getInstance().encrypt(attribute);
		} catch (EncryptDecryptException e) {
			logger.error("DbConnPwEncodeConverter : {} " , e.getMessage() , e);
		}
		return null;
	}

	@Override
	public String convertToEntityAttribute(String s) {
		if(s==null || "".equals(s)) return null;
		return s;
	}

}