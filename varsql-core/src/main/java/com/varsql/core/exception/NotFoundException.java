package com.varsql.core.exception;

/**
 * component not found exception
* 
* @fileName	: NotFoundException.java
* @author	: ytkim
 */
public class NotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public NotFoundException() {}
  
  public NotFoundException(Throwable cause) {
    super(cause);
  }
  
  public NotFoundException(String s) {
    super(s);
  }
  
  public NotFoundException(String s, Exception exeception) {
    super(s, exeception);
  }
}
