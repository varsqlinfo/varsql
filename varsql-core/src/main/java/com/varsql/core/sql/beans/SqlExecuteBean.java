package com.varsql.core.sql.beans;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * -----------------------------------------------------------------------------
* @fileName		: SqlExecuteBean.java
* @desc		: sql execute bean
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2022. 1. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class SqlExecuteBean {

	private long threadId;
	
	private List<Statement> statement;
	
	private long startTime;
	
	private long entTime;
	

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public List<Statement> getStatement() {
		return statement;
	}

	public void setStatement(List<Statement> statement) {
		this.statement = statement;
	}
	
	public void addStatement(Statement statement) {
		if(this.statement == null) {
			this.statement = new LinkedList<Statement>();
		}
		this.statement.add(statement);
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEntTime() {
		return entTime;
	}

	public void setEntTime(long entTime) {
		this.entTime = entTime;
	}
	
	@Override
	public String toString() {

		return new StringBuilder()
				.append("threadId : ").append(threadId)
				.append(", statement : ").append(statement)
				.append(" startTime : ").append(startTime)
				.append(" entTime : ").append(entTime)
				.toString();
	}
}

