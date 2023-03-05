package com.varsql.core.sql.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JdbcUtils {
	
	private final static int FETCH_COUNT = 1000;
	
	private JdbcUtils() {}

	public static void close(ResultSet rs) {
		close(null, null, rs);
	}

	public static void close(Statement stmt) {
		close(null, stmt, null);
	}

	public static void close(Statement stmt, ResultSet rs) {
		close(null, stmt, rs);
	}

	public static void close(Connection con) {
		close(con, null, null);
	}

	public static void close(Connection con, Statement stmt) {
		close(con, stmt, null);
	}

	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try {if (rs != null)  rs.close();} catch (SQLException e) {}
		try {if (stmt != null) stmt.close();} catch (Exception ex) {}
		try {if (con != null) con.close();} catch (SQLException e) {}
	}
	
	public static void setStatementFetchSize(Statement statement, int maxRow) throws SQLException {
		setStatementFetchSize(statement, maxRow, FETCH_COUNT);
	}
	
	public static void setStatementFetchSize(Statement statement, int maxRow, int fetchCount) throws SQLException {
		statement.setFetchSize(maxRow <= 0 ? fetchCount : (maxRow > fetchCount ? fetchCount : maxRow));
	}
}
