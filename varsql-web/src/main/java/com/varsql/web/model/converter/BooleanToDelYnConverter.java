package com.varsql.web.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToDelYnConverter implements AttributeConverter<Boolean, String> {
	
	@Override
    public String convertToDatabaseColumn(Boolean attribute) {
      return (attribute != null && attribute) ? "Y" : "N";
    }
	
	@Override
    public Boolean convertToEntityAttribute(String s) {
      return "Y".equals(s);
    }
}