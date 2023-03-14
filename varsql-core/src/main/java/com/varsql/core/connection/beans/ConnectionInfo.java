package com.varsql.core.connection.beans;

import java.io.Serializable;

import javax.persistence.Transient;

import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.db.meta.DBVersionInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConnectionInfo implements Serializable {

	private static final long serialVersionUID = -3088926375387532055L;

	private String connid;
	private DBVersionInfo version;
	private String aliasName;
	private String type;
	private String url;
	private String username;

	@Transient
	private String password;
	private int connectionTimeOut;
	private int exportCount;
	private int initialSize=5;
	private int maxActive=10;
	private int minIdle=3;
	private int maxIdle=5;
	
	private long maxWait=60000;
	
	private long timebetweenevictionrunsmillis;
	private boolean testWhileIdle;
	private String validationQuery;
	private String connectionOptions;
	
	private boolean enableConnectionPool;
	
	private boolean useColumnLabel;

	private JDBCDriverInfo jdbcDriverInfo;
	
	@Builder
	public ConnectionInfo(String connid, DBVersionInfo version, String aliasName, String type, String url, String username, String password
			, int connectionTimeOut, int exportCount, int initialSize, int maxActive, int minIdle, int maxIdle, long maxWait
			, long timebetweenevictionrunsmillis, boolean testWhileIdle, String validationQuery, String useColumnLabel
			, String connectionOptions, boolean enableConnectionPool, JDBCDriverInfo jdbcDriverInfo) {
		
		this.connid = connid;
		this.aliasName = aliasName;
		this.type = type;
		this.url = url;
		this.username = username;
		this.password = password;
		this.connectionTimeOut = connectionTimeOut;
		this.exportCount = exportCount;
		this.initialSize = initialSize;
		this.maxActive = maxActive;
		this.minIdle = minIdle;
		this.maxIdle = maxIdle;
		this.maxWait = maxWait;
		this.version = version;
		this.timebetweenevictionrunsmillis = timebetweenevictionrunsmillis;
		this.testWhileIdle = testWhileIdle;
		this.validationQuery = validationQuery;
		this.useColumnLabel = "Y".equals(useColumnLabel);
		this.connectionOptions = connectionOptions;
		this.enableConnectionPool = enableConnectionPool;
		this.jdbcDriverInfo = jdbcDriverInfo;
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();

	    result.append(this.getClass().getName() ).append( " Object { " ).append( BlankConstants.NEW_LINE);
	    result.append(" connid: ").append(connid).append(BlankConstants.NEW_LINE);
	    result.append(" version: ").append(version).append(BlankConstants.NEW_LINE);
	    result.append(" aliasName: ").append(aliasName).append(BlankConstants.NEW_LINE);
	    result.append(" type: " ).append( type ).append( BlankConstants.NEW_LINE);
	    result.append(" url: " ).append( url ).append( BlankConstants.NEW_LINE);
	    result.append(" username: " ).append( username ).append( BlankConstants.NEW_LINE);
	    result.append(" password: " ).append( password ).append( BlankConstants.NEW_LINE);
	    result.append(" initialSize: " ).append( initialSize ).append( BlankConstants.NEW_LINE);
	    result.append(" maxActive: " ).append( maxActive ).append( BlankConstants.NEW_LINE);
	    result.append(" minIdle: " ).append( minIdle ).append( BlankConstants.NEW_LINE);
	    result.append(" maxIdle: " ).append( maxIdle ).append( BlankConstants.NEW_LINE);
	    result.append(" maxWait: " ).append( maxWait ).append( BlankConstants.NEW_LINE);
	    result.append(" connection_opt: " ).append( connectionOptions ).append( BlankConstants.NEW_LINE);
	    result.append(" timebetweenevictionrunsmillis: " ).append( timebetweenevictionrunsmillis ).append( BlankConstants.NEW_LINE);
	    result.append(" useColumnLabel: " ).append( useColumnLabel ).append( BlankConstants.NEW_LINE);
	    result.append(" test_while_idle: " ).append( testWhileIdle ).append( BlankConstants.NEW_LINE);
	    result.append(" validation_query: " ).append( validationQuery ).append( BlankConstants.NEW_LINE);
	    result.append(" isDisablePool: " ).append( enableConnectionPool ).append( BlankConstants.NEW_LINE);
	    result.append("}");

	    return result.toString();
	}
}
