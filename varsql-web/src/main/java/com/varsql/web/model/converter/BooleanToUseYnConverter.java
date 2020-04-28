package com.varsql.web.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToUseYnConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return (attribute == null || attribute) ? "Y" : "N";
	}
	
	@Override
	public Boolean convertToEntityAttribute(String s) {
		return !"N".equals(s);
	}
	
}