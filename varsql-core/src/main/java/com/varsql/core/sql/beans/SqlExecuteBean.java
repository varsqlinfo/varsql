package com.varsql.core.sql.beans;

import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

@Getter
@Setter
@Builder
public class SqlExecuteBean {

	private String reqUid;
	
	private long threadId;
	
	private List<ExecuteStatementInfo> executeStatements;
	
	private String ip;
	
	private long startTime;
	
	private long entTime;
	
	public void setStatement(List<ExecuteStatementInfo> statement) {
		this.executeStatements = statement;
	}
	
	public void addStatement(ExecuteStatementInfo statement) {
		if(this.executeStatements == null) {
			this.executeStatements = new LinkedList<ExecuteStatementInfo>();
		}
		this.executeStatements.add(statement);
	}
	
	@Override
	public String toString() {

		return new StringBuilder()
				.append("threadId : ").append(threadId)
				.append(", executeStatements : ").append(executeStatements)
				.append(" startTime : ").append(startTime)
				.append(" entTime : ").append(entTime)
				.toString();
	}
}

