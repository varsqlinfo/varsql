package com.varsql.core.connection.beans;

public class JdbcURLFormatParam {
	
	private String serverIp;

	private int port;

	private String databaseName;

	private String optStr;
	
	public JdbcURLFormatParam(String serverIp, int port, String databaseName, String optStr) {
		this.serverIp = serverIp;
		this.port = port;
		this.databaseName = databaseName;
		this.optStr = optStr;
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public int getPort() {
		return this.port;
	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public String getOptStr() {
		return this.optStr;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setOptStr(String optStr) {
		this.optStr = optStr;
	}

	public static JdbcURLInfoBuilder builder() {
		return new JdbcURLInfoBuilder();
	}
	
	

	public static class JdbcURLInfoBuilder {

		private String serverIp;

		private int port;

		private String databaseName;

		private String optStr;

		public JdbcURLInfoBuilder serverIp(String serverIp) {
			this.serverIp = serverIp;
			return this;
		}

		public JdbcURLInfoBuilder port(int port) {
			this.port = port;
			return this;
		}

		public JdbcURLInfoBuilder databaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}

		public JdbcURLInfoBuilder optStr(String optStr) {
			this.optStr = optStr;
			return this;
		}

		public JdbcURLFormatParam build() {
			return new JdbcURLFormatParam( this.serverIp, this.port, this.databaseName, this.optStr);
		}

		public String toString() {
			return "serverIp=" + this.serverIp+ ", port=" + this.port + ", databaseName=" + this.databaseName + ", optStr=" + this.optStr + ")";
		}
	}
}
