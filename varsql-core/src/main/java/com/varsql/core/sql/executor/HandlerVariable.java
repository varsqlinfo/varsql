package com.varsql.core.sql.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandlerVariable {
	private int count = 0;
	private PreparedStatement statement;
	private Connection conn;
	private String sql;
	private int failIdx = 0;
	
	public HandlerVariable() {
	}

	public HandlerVariable(Connection conn) {
		this.conn = conn;
	}

	public int getCount() {
		return count;
	}
	public void addCount() {
		++this.count;
	}

	public PreparedStatement getStatement() throws SQLException {
		return this.statement;
	}
	public PreparedStatement getStatement(String sql) throws SQLException {
		if(statement == null) {
			this.sql = sql;
			statement = this.conn.prepareStatement(sql);
		}
		return statement;
	}

	public String getSql() {
		return sql;
	}

	public int getFailIdx() {
		return failIdx;
	}

	public void setFailIdx(int failIdx) {
		this.failIdx = failIdx;
	}
}