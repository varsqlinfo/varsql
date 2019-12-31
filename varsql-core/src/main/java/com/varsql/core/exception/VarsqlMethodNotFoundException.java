package com.varsql.core.exception;
 
/**
 * 
 * @FileName  : VarsqlException.java
 * @프로그램 설명 :
 * @Date      : 2018. 4. 3. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlMethodNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public VarsqlMethodNotFoundException() {
		super();
	}
	
	/**
	 * @param s java.lang.String
	 */
	public VarsqlMethodNotFoundException(String s) {
		super(s);
	}

	
}
