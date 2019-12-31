package com.varsql.core.sql.builder;

import java.util.List;

public class SqlSource {
	private String commandType;
	private VarsqlStatementType statementType;
	private String query;
	private String calluserid;
	private SqlSourceResultVO result;
	private List param;
	
	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		 
		this.commandType = commandType != null? commandType.toUpperCase() : VarsqlCommandType.UNKNOWN.name();
		this.statementType = VarsqlStatementType.PREPARED;
		
		if(this.commandType.equalsIgnoreCase(VarsqlCommandType.CALL.name())){
			this.statementType = VarsqlStatementType.CALLABLE;
		}
	}

	public VarsqlStatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(VarsqlStatementType statementType) {
		this.statementType = statementType;
	}

	public String getCalluserid() {
		return calluserid;
	}

	public void setCalluserid(String calluserid) {
		this.calluserid = calluserid;
	}
	
	public SqlSourceResultVO getResult() {
		return result;
	}

	public void setResult(SqlSourceResultVO result) {
		this.result = result;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public List getParam() {
		return param;
	}

	public void setParam(List param) {
		this.param = param;
	}
	
	@Override
	public String toString() {
		return new StringBuffer()
			.append("calluserid : [").append(calluserid)
			.append("] result : [").append( result)
			.append("] commandType : [").append(commandType)
			.append("] statementType : [").append(statementType)
			.append("] query : [").append(query)
			.append("] param : [").append(param)
			.append("]")
			.toString();
	}

	
}

