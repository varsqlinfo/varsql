package com.varsql.core.db.ddl.conversion;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversionInfo extends HashMap<String, HashMap<String,ConversionType>> {
	private static final long serialVersionUID = 1L;
		
	@Override
	public HashMap<String, ConversionType> put(String key, HashMap<String, ConversionType> value) {
		return super.put(key.toUpperCase(), value);
	}
}
