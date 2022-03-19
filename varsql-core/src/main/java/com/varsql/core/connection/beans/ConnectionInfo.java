package com.varsql.core.connection.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;
import javax.sql.DataSource;

import com.varsql.core.common.util.CommUtils;


public class ConnectionInfo implements Serializable {

	private static final long serialVersionUID = -3088926375387532055L;

	private String connid;
	private String aliasName;
	private String type;
	private String url;
	private String username;

	@Transient
	private String password;
	private int connectionTimeOut;
	private int exportCount;
	private int maxActive=10;
	private int minIdle=3;
	private long maxWait=60000;
	private String version;
	private long timebetweenevictionrunsmillis;
	private boolean testWhileIdle;
	private String validationQuery;
	private Map connectionOptions;
	private Map poolOptions;
	private DataSource datasource;

	private JDBCDriverInfo jdbcDriverInfo;

	public String getConnid() {
		return connid;
	}

	public void setConnid(String connid) {
		this.connid = connid;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public long getTimebetweenevictionrunsmillis() {
		return timebetweenevictionrunsmillis;
	}

	public void setTimebetweenevictionrunsmillis(
			long timebetweenevictionrunsmillis) {
		this.timebetweenevictionrunsmillis = timebetweenevictionrunsmillis;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public JDBCDriverInfo getJdbcDriverInfo() {
		return jdbcDriverInfo;
	}

	public void setJdbcDriverInfo(JDBCDriverInfo jdbcDriverInfo) {
		this.jdbcDriverInfo = jdbcDriverInfo;
	}
	
	public long getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public int getExportCount() {
		return exportCount;
	}

	public void setExportCount(int exportCount) {
		this.exportCount = exportCount;
	}

	public Map getConnectionOptions() {
		return connectionOptions;
	}

	public void setConnectionOptions(String connectionOptions) {
		if(null == connectionOptions || "".equals(connectionOptions)) return ;

		String [] tmpOpt = CommUtils.split(connectionOptions, ";");

		if(this.connectionOptions ==null) this.connectionOptions=new HashMap();

		String [] optVal = null;
		String tmpKey = "";
		for (int i = 0; i < tmpOpt.length; i++) {

			tmpKey=tmpOpt[i];

			if("".equals(tmpKey.trim())){
				continue;
			}

			optVal = CommUtils.split(tmpKey, "=");

			this.connectionOptions.put(optVal[0], ( optVal.length > 1 ? optVal[1]:"" ) );
		}
	}

	public Map getPoolOptions() {
		return poolOptions;
	}

	public void setPoolOptions(String poolOption) {
		if(null == poolOption || "".equals(poolOption)) return ;

		String [] tmpOpt = CommUtils.split(poolOption, ";");

		if(this.poolOptions ==null) this.poolOptions=new HashMap();

		String [] optVal = null;
		String tmpKey = "";
		for (int i = 0; i < tmpOpt.length; i++) {

			tmpKey=tmpOpt[i];

			if(!"".equals(tmpKey.trim())){
				optVal = CommUtils.split(tmpKey, "=");
				this.poolOptions.put(optVal[0], ( optVal.length > 1 ?optVal[1]:"" ) );
			}
		}
	}


	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");

	    result.append(this.getClass().getName() ).append( " Object { " ).append( NEW_LINE);
	    result.append(" connid: ").append(connid).append(NEW_LINE);
	    result.append(" aliasName: ").append(aliasName).append(NEW_LINE);
	    result.append(" type: " ).append( type ).append( NEW_LINE);
	    result.append(" url: " ).append( url ).append( NEW_LINE);
	    result.append(" username: " ).append( username ).append( NEW_LINE);
	    result.append(" password: " ).append( password ).append( NEW_LINE);
	    result.append(" maxActive: " ).append( maxActive ).append( NEW_LINE);
	    result.append(" minIdle: " ).append( minIdle ).append( NEW_LINE);
	    result.append(" maxWait: " ).append( maxWait ).append( NEW_LINE);
	    result.append(" connection_opt: " ).append( connectionOptions ).append( NEW_LINE);
	    result.append(" pool_opt: " ).append( poolOptions ).append( NEW_LINE);
	    result.append(" timebetweenevictionrunsmillis: " ).append( timebetweenevictionrunsmillis ).append( NEW_LINE);
	    result.append(" test_while_idle: " ).append( testWhileIdle ).append( NEW_LINE);
	    result.append(" validation_query: " ).append( validationQuery ).append( NEW_LINE);
	    result.append("}");

	    return result.toString();
	}

	
}
