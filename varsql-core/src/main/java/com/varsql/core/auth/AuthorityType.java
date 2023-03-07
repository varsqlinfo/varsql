package com.varsql.core.auth;

public interface AuthorityType {
	
	public String getName();
	
	public int getPriority();
	
	public String geti18N();

	public String mainPage();
	
	public String getMainPage();
}