package com.varsql.web.exception;

/**
 * component not found exception
* 
* @fileName	: ComponentNotFoundException.java
* @author	: ytkim
 */
public class TaskNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public TaskNotFoundException() {}
  
  public TaskNotFoundException(Throwable cause) {
    super(cause);
  }
  
  public TaskNotFoundException(String s) {
    super(s);
  }
  
  public TaskNotFoundException(String s, Exception exeception) {
    super(s, exeception);
  }
}
