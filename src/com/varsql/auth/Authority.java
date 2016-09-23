package com.varsql.auth;

public enum Authority {
	GUEST(-1), USER(100), MANAGER(200), ADMIN(999);
	
	int priority = -1;
	
	Authority(int _priority){
		this.priority = _priority;
	}
	
	public int getPriority(){
		return this.priority;
	}

	public String mainPage() {
		String mainPage = "/guest/";
		switch (this) {
		case GUEST:
			mainPage = "/guest/";
			break;
		case USER:
			mainPage = "/user/";
			break;
		case MANAGER:
			mainPage = "/manager/";
			break;
		case ADMIN:
			mainPage = "/admin/";
			break;
		}
		
		return mainPage;
	}
}