package com.varsql.core.pattern.convert;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.varsql.core.pattern.parsing.TokenInfo;

public interface Converter {
	public String transform(String cont, TokenInfo... tokens);
	public String transform(String cont, TokenHandler handler, TokenInfo... tokens);
	
	public List<T> tokenData(String cont, TokenInfo... tokens);
	public List<T> tokenData(String cont, TokenHandler handler, TokenInfo... tokens);
	
}