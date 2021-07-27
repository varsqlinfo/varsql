package com.varsql.core.sql.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JdbcUtils {
	private JdbcUtils() {}

	public static void close(ResultSet rs) {
		close(null, null, rs);
	}

	public static void close(Statement stmt) {
		close(null, stmt, null);
	}

	public static void close(Connection con) {
		close(con, null, null);
	}

	public static void close(Statement stmt, ResultSet rs) {
		close(null, stmt, rs);
	}

	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try {if (rs != null)  rs.close();} catch (SQLException e) {}
		try {if (stmt != null) stmt.close();} catch (Exception ex) {}
		try {if (con != null) con.close();} catch (SQLException e) {}
	}
}
