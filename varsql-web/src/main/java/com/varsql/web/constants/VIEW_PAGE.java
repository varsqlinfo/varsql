package com.varsql.web.constants;

public enum VIEW_PAGE{
	DATABASE ("/database")
	, DATABASE_MENU ("/database/menu")
	, DATABASE_EXTENSION ("/database/extension")
	, DATABASE_PREFERENCES ("/database/preferences")
	, DATABASE_TOOLS ("/database/tools")
	, DATABASE_UTILS ("/database/utils")
	, LOGIN("/login")
	, JOIN("/join")
	, GUEST("/guest")
	, HELP("/help")
	, COMMON("/common")
	, USER("/user")
	, USER_PREFERENCES("/user/preferences")
	, MANAGER("/manager")
	, MANAGER_SCHEDULER("/manager/scheduler")
	, MANAGER_GROUP("/manager/group")
	, MANAGER_BOARD("/manager/board")
	, MANAGER_lOG("/manager/log")
	, MANAGER_TASK("/manager/task")
	, ADMIN("/admin")
	, SETUP("/setup")

	,BOARD("/board");

	private String urlPrefix;

	VIEW_PAGE(String prefix){
		this.urlPrefix = prefix;
	}

	public String getPrefix() {
		return urlPrefix;
	}

	public String getViewPage(String path) {
		return String.format("%s%s", urlPrefix, path);
	}
}