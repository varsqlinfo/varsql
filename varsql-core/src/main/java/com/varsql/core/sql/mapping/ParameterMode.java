package com.varsql.core.sql.mapping;

//TODO 0927 new 
public enum ParameterMode {
  IN, OUT, INOUT;
  
  public static ParameterMode getParameterMode(String modeStr) {
	  for(ParameterMode mode :  values()){
		  if(mode.name().equalsIgnoreCase(modeStr)) {
			  return mode; 
		  }
	  }
	  
	  return null; 
  }
}
