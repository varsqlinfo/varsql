package com.varsql.core.common.constants;

public enum PathType {
	FILE("file"), PATH("path");
	
	private String code;
	
	PathType(String code) {
		this.code = code; 
	}
	
	public String getCode () {
		return this.code;
	}
	
	public static PathType getPathType(String pathType) {
		for (PathType pathTypeInfo : PathType.values()) {
			if(pathTypeInfo.getCode().equals(pathType)) {
				return pathTypeInfo;
			}
		}
		
		return null;
	}
}
