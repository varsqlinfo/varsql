package com.varsql.core.pattern.convert;

import java.util.List;

/**
 * 
 * @FileName  : ConvertResult.java
 * @프로그램 설명 : ConvertResult
 * @Date      : 2017. 10. 31. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class ConvertResult{
	
	private String cont;
	
	private List<?> parameterInfo;
	
	public ConvertResult(String cont) {
		this.cont = cont; 
	}
	
	public ConvertResult(String cont, List<?> parameterInfo) {
		this.cont = cont; 
		this.parameterInfo = parameterInfo; 
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public List<?> getParameterInfo() {
		return parameterInfo;
	}

	public void setParameterInfo(List<?> parameterInfo) {
		this.parameterInfo = parameterInfo;
	}
	
	
	
}
