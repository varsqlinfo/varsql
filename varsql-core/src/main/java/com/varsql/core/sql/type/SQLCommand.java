package com.varsql.core.sql.type;

public interface SQLCommand {
	
	public SQLCommandType getCommand();
	public String checkSql(String sql);
}
