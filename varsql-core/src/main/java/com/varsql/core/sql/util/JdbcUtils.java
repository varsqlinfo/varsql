package com.varsql.core.sql.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.zaxxer.hikari.util.UtilityElf.DefaultThreadFactory;

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
	
	public static void connectionCheck(String driverId, String driverClass, String dbType, String connectionUrl, Properties p,
			List<FileInfo> driverJarFiles, String validationQuery, int networkTimeout, int queryTimeout) throws ClassNotFoundException, IOException, SQLException, InstantiationException, IllegalAccessException {

		PreparedStatement pstmt = null;

		Connection connChk = null;
		try {
			Driver dbDriver = JdbcDriverLoader.checkDriver(JDBCDriverInfo.builder().driverId(driverId)
					.driverClass(driverClass).driverFiles(driverJarFiles).build());
			
			Executor executor= null; 
			if (dbType.equalsIgnoreCase("mysql")) {
				executor = new MysqlExecutor();
			} else {
				executor = Executors.newSingleThreadExecutor();
			}
			
			connChk = dbDriver.connect(connectionUrl, p);
			connChk.setNetworkTimeout(executor, (int) TimeUnit.SECONDS.toMillis(networkTimeout));
			pstmt = connChk.prepareStatement(validationQuery);

			pstmt.setQueryTimeout(queryTimeout);

			pstmt.executeQuery();
			connChk.close();
			
		}finally {
			JdbcUtils.close(connChk, pstmt, null);
		}

	}
	
	private static class MysqlExecutor implements Executor {
		@Override
		public void execute(Runnable command) {
			try {
				command.run();
			} catch (Exception ex) {
				LoggerFactory.getLogger(JdbcUtils.class).error("Failed to execute: {}", command, ex);
			}
		}
	}
}
