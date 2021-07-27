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
	private String driver;
	private String url;
	private String username;

	@Transient
	private String password;
	private int connectionTimeOut;
	private int exportCount;
	private int max_active=10;
	private int min_idle=3;
	private long max_wait=60000;
	private String version;
	private long timebetweenevictionrunsmillis;
	private String test_while_idle;
	private String validation_query;
	private Map connection_opt;
	private Map pool_opt;
	private DataSource datasource;

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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
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

	public int getMax_active() {
		return max_active;
	}

	public void setMax_active(int max_active) {
		this.max_active = max_active;
	}

	public long getTimebetweenevictionrunsmillis() {
		return timebetweenevictionrunsmillis;
	}

	public void setTimebetweenevictionrunsmillis(
			long timebetweenevictionrunsmillis) {
		this.timebetweenevictionrunsmillis = timebetweenevictionrunsmillis;
	}

	public String getTest_while_idle() {
		return test_while_idle;
	}

	public void setTest_while_idle(String test_while_idle) {
		this.test_while_idle = test_while_idle;
	}

	public String getValidation_query() {
		return validation_query;
	}

	public void setValidation_query(String validation_query) {
		this.validation_query = validation_query;
	}

	public Map getConnection_opt() {
		return connection_opt;
	}

	public void setConnection_opt(String connection_opt) {
		if(null == connection_opt || "".equals(connection_opt)) return ;

		String [] tmpOpt = CommUtils.split(connection_opt, ";");

		if(this.connection_opt ==null) this.connection_opt=new HashMap();

		String [] optVal = null;
		String tmpKey = "";
		for (int i = 0; i < tmpOpt.length; i++) {

			tmpKey=tmpOpt[i];

			if("".equals(tmpKey.trim())){
				continue;
			}

			optVal = CommUtils.split(tmpKey, "=");

			this.connection_opt.put(optVal[0], ( optVal.length > 1 ? optVal[1]:"" ) );
		}
	}

	public Map getPool_opt() {
		return pool_opt;
	}

	public void setPool_opt(String pool_opt) {
		if(null == pool_opt || "".equals(pool_opt)) return ;

		String [] tmpOpt = CommUtils.split(pool_opt, ";");

		if(this.pool_opt ==null) this.pool_opt=new HashMap();

		String [] optVal = null;
		String tmpKey = "";
		for (int i = 0; i < tmpOpt.length; i++) {

			tmpKey=tmpOpt[i];

			if("".equals(tmpKey.trim())){
				continue;
			}

			optVal = CommUtils.split(tmpKey, "=");
			this.pool_opt.put(optVal[0], ( optVal.length > 1 ?optVal[1]:"" ) );
		}
	}

	public int getMin_idle() {
		return min_idle;
	}

	public void setMin_idle(int min_idle) {
		this.min_idle = min_idle;
	}

	public long getMax_wait() {
		return max_wait;
	}

	public void setMax_wait(int max_wait) {
		this.max_wait = max_wait;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");

	    result.append(this.getClass().getName() ).append( " Object { " ).append( NEW_LINE);
	    result.append(" connid: ").append(connid).append(NEW_LINE);
	    result.append(" aliasName: ").append(aliasName).append(NEW_LINE);
	    result.append(" type: " ).append( type ).append( NEW_LINE);
	    result.append(" driver: " ).append( driver ).append( NEW_LINE);
	    result.append(" url: " ).append( url ).append( NEW_LINE);
	    result.append(" username: " ).append( username ).append( NEW_LINE);
	    result.append(" password: " ).append( password ).append( NEW_LINE);
	    result.append(" max_active: " ).append( max_active ).append( NEW_LINE);
	    result.append(" min_idle: " ).append( min_idle ).append( NEW_LINE);
	    result.append(" max_wait: " ).append( max_wait ).append( NEW_LINE);
	    result.append(" connection_opt: " ).append( connection_opt ).append( NEW_LINE);
	    result.append(" pool_opt: " ).append( pool_opt ).append( NEW_LINE);
	    result.append(" timebetweenevictionrunsmillis: " ).append( timebetweenevictionrunsmillis ).append( NEW_LINE);
	    result.append(" test_while_idle: " ).append( test_while_idle ).append( NEW_LINE);
	    result.append(" validation_query: " ).append( validation_query ).append( NEW_LINE);
	    result.append("}");

	    return result.toString();
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
}
