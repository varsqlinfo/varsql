package com.varsql.core.auth;

public enum AuthorityTypeImpl implements AuthorityType{
	GUEST(-1, "screen.guest","/guest/"), USER(100, "screen.user","/user/"), MANAGER(500, "screen.manager","/manager/"), ADMIN(999, "screen.admin","/admin/"), SYSTEM(-999, null, null);
	
	int priority = -1;
	String i18N = "";
	String mainPage = "";
	
	AuthorityTypeImpl(int _priority, String _i18N , String _mainPage){
		this.priority = _priority;
		this.i18N = _i18N;
		this.mainPage = _mainPage;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public String geti18N(){
		return this.i18N;
	}

	public String mainPage() {
		return mainPage;
	}
	public String getMainPage() {
		return mainPage;
	}

	@Override
	public String getName() {
		return this.name();
	}
}