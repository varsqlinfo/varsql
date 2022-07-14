package com.varsql.core.sql.type;

public interface CommandTypeFactory {
	public SQLCommand getCommandType(SQLCommandType command);
}

