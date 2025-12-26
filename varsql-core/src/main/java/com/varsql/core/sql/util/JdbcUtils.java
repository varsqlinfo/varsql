package com.varsql.core.sql.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import com.varsql.core.common.util.JdbcDriverLoader;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datasource.SingleConnectionDataSource;
import com.varsql.core.db.datasource.SingleDriverDataSource;
import com.varsql.core.exception.ConnectionException;
import com.vartech.common.app.beans.FileInfo;

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

		boolean ok = JdbcNetworkChecker.check(connectionUrl, DBVenderType.getDBType(dbType),  (int) TimeUnit.SECONDS.toMillis(networkTimeout));
		
		if (!ok) {
		    throw new IllegalStateException("DB network unreachable");
		}
		
		PreparedStatement pstmt = null;

		Connection connChk = null;
		try {
			Driver dbDriver = JdbcDriverLoader.checkDriver(JDBCDriverInfo.builder().driverId(driverId)
					.driverClass(driverClass).driverFiles(driverJarFiles).build());
			
			connChk = dbDriver.connect(connectionUrl, p);
			
			setNetworkTimeout(connChk, dbType, networkTimeout);
			
			pstmt = connChk.prepareStatement(validationQuery);

			pstmt.setQueryTimeout(queryTimeout);

			pstmt.executeQuery();
			connChk.close();
			
		}finally {
			JdbcUtils.close(connChk, pstmt, null);
		}
	}
	
	/**
	 * set network timeout
	 *  
	 * @param connChk connection 
	 * @param dbType	db type
	 * @param networkTimeout	timeout value
	 * @throws SQLException
	 */
	public static void setNetworkTimeout(Connection connChk, String dbType, int networkTimeout) throws SQLException {
		if(hasMethodName(connChk.getClass(), "setNetworkTimeout")) {
			
			Executor executor= null; 
			if (dbType.equalsIgnoreCase("mysql")) {
				executor = new MysqlExecutor();
			} else {
				executor = Executors.newSingleThreadExecutor();
			}
			
			connChk.setNetworkTimeout(executor, (int) TimeUnit.SECONDS.toMillis(networkTimeout));
		}
		
	}

	public static boolean connectionTest(DBVenderType dbType, String driverClass, String connectionUrl,  String user,
			 String pw, int networkTimeout, int queryTimeout) throws ClassNotFoundException, IOException, SQLException, InstantiationException, IllegalAccessException {
		
		
		boolean ok = JdbcNetworkChecker.check(connectionUrl, dbType,  (int) TimeUnit.SECONDS.toMillis(networkTimeout));
		
		if (!ok) {
		    throw new IllegalStateException("DB network unreachable");
		}

		PreparedStatement pstmt = null;
		
		Connection connChk = null;
		try {
			
			Class.forName(driverClass);
			connChk= DriverManager.getConnection(connectionUrl, user, pw);
			
			setNetworkTimeout(connChk, dbType.getName(), networkTimeout);
			
			pstmt = connChk.prepareStatement(ValidationProperty.getInstance().validationQuery(dbType));
			
			pstmt.setQueryTimeout(queryTimeout);
			
			pstmt.executeQuery();
			connChk.close();
			
			return true; 
		}finally {
			JdbcUtils.close(connChk, pstmt, null);
		}
	}
	
	public static DataSource getDataSource(ConnectionInfo connInfo) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		Driver dbDriver = null;
		
		if (connInfo.getJdbcDriverInfo() != null) {
			dbDriver = JdbcDriverLoader.getInstance().load(connInfo.getJdbcDriverInfo());
			if (dbDriver == null) {
				throw new ConnectionException("jdbc driver load fail : " + connInfo.getJdbcDriverInfo());
			}
		}
		
    	if(dbDriver != null) {
    		return new SingleDriverDataSource(dbDriver, connInfo.getUrl(), connInfo.getUsername(), connInfo.getPassword());
    	}else {
			return new SingleConnectionDataSource(connInfo.getJdbcDriverInfo().getDriverClass(), connInfo.getUrl(), connInfo.getUsername(), connInfo.getPassword());
    	}
	}
	
	private static boolean hasMethodName(Class clazz, String methodName) {
		Method[] methods = clazz.getClass().getMethods();
		for (Method m : methods) {
		  if (m.getName().equals(methodName)) {
		    return true;
		  }
		}
		
		return false; 
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
