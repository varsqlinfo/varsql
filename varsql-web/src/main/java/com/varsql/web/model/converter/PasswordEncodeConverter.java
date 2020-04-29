package com.varsql.web.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.varsql.web.util.BeanUtils;

@Converter
public class PasswordEncodeConverter implements AttributeConverter<String, String> {
	
	@Override
	public String convertToDatabaseColumn(String attribute) {
		System.out.println("11111111111111 : "+ attribute);
		
		return ((PasswordEncoder)BeanUtils.getBean("varsqlPasswordEncoder")).encode(attribute);
	}
	
	@Override
	public String convertToEntityAttribute(String s) {
		return s;
	}
	
}