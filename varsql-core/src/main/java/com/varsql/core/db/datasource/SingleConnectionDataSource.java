package com.varsql.core.db.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.vartech.common.utils.VartechReflectionUtils;


/**
 * 단일 커넥션 데이터 소스
 * 
 * 
 * @author ytkim
 *
 */
public class SingleConnectionDataSource implements DataSource {
	
	private String url;

	private String username;

	private String password;

	private String catalog;

	private String schema;

	private Properties connectionProperties;
	
	private String jdbcDriverClass; 


	public SingleConnectionDataSource(String jdbcDriverClass, String url) {
		setJdbcDriverClass(jdbcDriverClass);
		setUrl(url);
	}

	public SingleConnectionDataSource(String jdbcDriverClass, String url, String username, String password) {
		setJdbcDriverClass(jdbcDriverClass);
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}

	public SingleConnectionDataSource(String jdbcDriverClass, String url, Properties conProps) {
		setJdbcDriverClass(jdbcDriverClass);
		setUrl(url);
		setConnectionProperties(conProps);
	}
	
	public void setUrl( String url) {
		this.url = (url != null ? url.trim() : null);
	}

	public String getUrl() {
		return this.url;
	}

	public void setUsername( String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setPassword( String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setCatalog( String catalog) {
		this.catalog = catalog;
	}

	public String getCatalog() {
		return this.catalog;
	}

	public void setSchema( String schema) {
		this.schema = schema;
	}

	public String getSchema() {
		return this.schema;
	}

	public void setConnectionProperties( Properties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public Properties getConnectionProperties() {
		return this.connectionProperties;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getConnectionFromDriver(getUsername(), getPassword());
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnectionFromDriver(username, password);
	}

	protected Connection getConnectionFromDriver( String username,  String password) throws SQLException {
		Properties mergedProps = new Properties();
		Properties connProps = getConnectionProperties();
		if (connProps != null) {
			mergedProps.putAll(connProps);
		}
		if (username != null) {
			mergedProps.setProperty("user", username);
		}
		if (password != null) {
			mergedProps.setProperty("password", password);
		}

		Connection con = getConnectionFromDriver(mergedProps);
		if (this.catalog != null) {
			con.setCatalog(this.catalog);
		}
		if (this.schema != null) {
			con.setSchema(this.schema);
		}
		return con;
	}
	
	public void setJdbcDriverClass(String jdbcDriverClass) {
		this.jdbcDriverClass = jdbcDriverClass;
	}

	public String getJdbcDriverClass() {
		return this.jdbcDriverClass;
	}

	protected Connection getConnectionFromDriver(Properties props) throws SQLException {
		VartechReflectionUtils.forName(this.jdbcDriverClass);
		return DriverManager.getConnection(this.url, props);
	}
	
	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public void setLoginTimeout(int timeout) throws SQLException {
		throw new UnsupportedOperationException("setLoginTimeout");
	}

	@Override
	public PrintWriter getLogWriter() {
		throw new UnsupportedOperationException("getLogWriter");
	}

	@Override
	public void setLogWriter(PrintWriter pw) throws SQLException {
		throw new UnsupportedOperationException("setLogWriter");
	}


	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
			return (T) this;
		}
		throw new SQLException("DataSource cannot be unwrapped : " + iface.getName() );
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	@Override
	public Logger getParentLogger() {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

}