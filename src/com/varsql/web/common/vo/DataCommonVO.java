package com.varsql.web.common.vo;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @FileName : DataCommonVO.java
 * @Author   : ytkim
 * @Program desc : 공통으로 사용할  VO
 * @Hisotry :
 */
public class DataCommonVO extends java.util.HashMap{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2614193288989664357L;

	public DataCommonVO(){
		super();
	}
	
	/**
	 * 
	 * @param key java.lang.String
	 * @param value java.lang.String
	 */
	public synchronized void put(String key, String value){
		super.put(key, value);
	}
	
	/**
	 * @return java.lang.String
	 * @param key java.lang.String
	 */
	public String getString(String key){
		Object o = (Object)super.get(key);
		if ( o == null ) return  "";
		else if( o.getClass().isArray() ) return ( Array.getLength(o) > 0 ?  ( Array.get(o, 0)==null?"":Array.get(o, 0).toString() ) : "" );
		else return  o.toString();
	}
	/**
	 * 
	 * @param key
	 * @param initVal
	 * @return String
	 */
	public String getString(String key , String initVal){
		String tmpVal =getString(key);
		return (!"".equals(tmpVal))?tmpVal:initVal;
	}
	
	/**
	 * 
	 * @param key
	 * @return  String
	 */
	public String getTrimString(String key){
		return trim (getString(key) );
	}
	
	/**
	 * 
	 * @param key
	 * @param delim
	 * @return String[]
	 */
	public String[] split(String key, String delim){
		return split( key,"" ,delim);
	}
	
	/**
	 * @param key
	 * @param initVal 
	 * @param delim
	 * @return String[]
	 */
	public String[] split(String key, String initVal, String delim){
		String a = getString(key, initVal);
		
		if("".equals(a)) return new String[0];
		
		int position=0;
		int delimiterIdx = 0; 
		int strLen = a.length();
		List<String> resultList = new ArrayList<String>();
		
		int len = delim.length();
		while(position <= strLen){
			delimiterIdx = a.indexOf(delim,position);
			if(delimiterIdx > -1){
				resultList.add(a.substring(position, delimiterIdx));
			}else {
				resultList.add(a.substring(position, strLen));
				break;
			}
			position = delimiterIdx+len;
		}
				
		return (String[]) resultList.toArray(new String[]{});
	}

	/**
	 * @return String[]
	 * @param key java.lang.String
	 */
	public String[] getArray(String key){
		return (String[])super.get(key);
	}
	
	/**
	 * @return boolean
	 * @param key java.lang.String
	 */
	public boolean getBoolean(String key){
		String value = getString(key);
		try {
			if ( "".equals(value) )	return false;
			else return  (new Boolean(getString(key))).booleanValue();
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * @return double
	 * @param key java.lang.String
	 */
	public double doubleValue(String key){
		String value = removeComma(getString(key));
	
		if ( "".equals(value) )	return 0;
	
		try {
			return Double.valueOf(value).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * @return float
	 * @param key java.lang.String
	 */
	
	public float floatValue(String key){
		return (float)doubleValue(key);
	}
	
	/**
	 * @return int
	 * @param key java.lang.String
	 */
	public int getInt(String key){
		return getInt(key, -1);
	}
	
	/**
	 * @return int
	 * @param key java.lang.String  
	 * @param initVal  
	 */
	public int getInt(String key , int initVal){
		String value = getString(key);
		
		return !numberChk(value)?initVal: (new Integer(value)).intValue();
	}
	
	/**
	 * @return long
	 * @param key java.lang.String
	 */
	public long longValue(String key){
		String value = removeComma(getString(key));
	
		if ( "".equals(value) ){
			return 0L;
		}
		
		long lvalue = 0L;
		
		try{
			lvalue = Long.valueOf(value).longValue();
		}catch(Exception e){
			lvalue = 0L;
		}
	
		return lvalue;
	}
	
	/**
	 * 
	 * @param str
	 * @return boolean
	 */
	public boolean numberChk(String str){
	    char c;

	    if("".equals(str)) return false;
	    
	    for(int i = 0 ; i < str.length() ; i++){
	        c = str.charAt(i);
	        if(c < 48 || c > 59){
	        	return false;
	        }
	    }
	    return true;
	}
	
	
	/**
	 * @return Integer
	 * @param key java.lang.String
	 */
	public Integer getInteger(String key) {
		return new Integer(getInt(key));
	}

	/**
	 * @return Long
	 * @param key java.lang.String
	 */
	public Long getLong(String key) {
		return new Long(longValue(key));
	}

	/**
	 * @return Double
	 * @param key java.lang.String
	 */
	public Double getDouble(String key) {
		return new Double(doubleValue(key));
	}
	
	/**
	 * @return BigDecimal
	 * @param key java.lang.String
	 */
	public BigDecimal getBigDecimal(String key) {

		String value = removeComma(getString(key));

		if ( "".equals(value) ){
			return new BigDecimal(0);
		}

		try{
			return new BigDecimal(value);
		}catch(Exception e){
			return new BigDecimal(0);
		}
	}
	/**
	 * remove "," in string.
	 * @return String
	 * @param s java.lang.String
	 */
	private String removeComma(String s){
		if ( s == null ){
			return null;
		}
		return s.replaceAll("\\,", "");
	}
	
	/**
	 * space remove
	 * "te st"
	 * @param s
	 * @return String "test"
	 */
	
	public String trim(String s) {
		
		if ( s == null ) return ""; 
			
		return s.replaceAll("\\p{Space}", "");
	}
}
