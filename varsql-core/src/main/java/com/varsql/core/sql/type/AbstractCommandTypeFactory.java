package com.varsql.core.sql.type;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractCommandTypeFactory implements CommandTypeFactory {
	
	protected AbstractCommandTypeFactory() {};
	
	private ConcurrentMap<SQLCommandType , SQLCommand> venderCommandType = new ConcurrentHashMap<SQLCommandType , SQLCommand>();
	
	
	public SQLCommand getCommandType(SQLCommandType command) {
		if(command == null) return SQLCommandType.OTHER;
		
		if(venderCommandType.containsKey(command)) {
			return venderCommandType.get(command);
		}
		
		return command;
	}
	
	public void addCommandType(SQLCommand command) {
		venderCommandType.put(command.getCommand(), command);
	};

}

