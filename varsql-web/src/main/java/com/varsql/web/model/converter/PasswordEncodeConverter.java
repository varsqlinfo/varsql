package com.varsql.web.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.varsql.web.util.PasswordUtils;

@Converter
public class PasswordEncodeConverter implements AttributeConverter<String, String> {
	
	@Override
	public String convertToDatabaseColumn(String attribute) {
		return PasswordUtils.encode(attribute);
	}
	
	@Override
	public String convertToEntityAttribute(String s) {
		return s;
	}
	
}