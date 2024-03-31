package com.varsql.core.db.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;


/**
 * simple datasource
 * 
 * @author ytkim
 *
 */
public class SimpleDataSource implements DataSource {
	
	private String url;

	private String username;

	private String password;

	private String catalog;

	private String schema;

	private Properties connectionProperties;

	private Driver driver;

	public SimpleDataSource(Driver driver, String url) {
		setDriver(driver);
		setUrl(url);
	}

	public SimpleDataSource(Driver driver, String url, String username, String password) {
		setDriver(driver);
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}

	public SimpleDataSource(Driver driver, String url, Properties conProps) {
		setDriver(driver);
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

	public void setDriver( Driver driver) {
		this.driver = driver;
	}

	public Driver getDriver() {
		return this.driver;
	}


	protected Connection getConnectionFromDriver(Properties props) throws SQLException {
		Driver driver = getDriver();
		String url = getUrl();
		return driver.connect(url, props);
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