package com.varsql.core.sql.beans;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ExecuteStatementInfo {
	
	@JsonIgnore
	private Connection conn;
	
	@JsonIgnore
	private Statement statement;
	
	private String sql;
	
	@Override
	public String toString() {

		return new StringBuilder()
				.append(" statement : ").append(statement)
				.append(", sql : ").append(sql)
				.toString();
	}

	public void abortConnection() throws SQLException {
		if(this.conn != null) {
			this.conn.abort(Executors.newSingleThreadExecutor());
		}
		
	}
}

