package com.varsql.core.common.constants;

import com.varsql.core.configuration.Configuration;

public interface VarsqlConstants {
	String RUNTIME = System.getProperty("varsql.runtime");
	String CHAR_SET = Configuration.getInstance().getProperties().getProperty("varsql.default.charset","utf-8");
	final String CONN_ID = "VCONNID";
	final String CONN_NAME = "VNAME";
	final String CONN_TYPE = "VTYPE";
	final String CONN_DBSCHEMA = "VDBSCHEMA";
	final String CONN_DRIVER = "VDRIVER";
	
	final String CONN_URL = "VURL";			// jdbc url
	final String CONN_VID = "VID";			// id
	final String CONN_PW = "VPW";			// db pasword
	
	final String CONN_SERVERIP = "VSERVERIP";	// db server ip 
	final String CONN_PORT = "VPORT";				// db port
	final String CONN_DATABASENAME = "VDATABASENAME";				// db port
	final String CONN_URL_DIRECT_YN = "URL_DIRECT_YN";	// url direct connection info
	
	final String CONN_URL_FORMAT = "URL_FORMAT";		// jdbc url format
	final String CONN_DEFAULT_PORT = "DEFAULT_PORT";	// db default port
	final String CONN_SCHEMA_TYPE = "SCHEMA_TYPE";		// schema type {user, db, default schema name (ex : public...)}
	
	final String CONN_QUERY = "VQUERY";
	final String CONN_POOLOPT = "VPOOLOPT";
	final String CONN_CONNOPT = "VCONNOPT";
	final String CONN_VDBVERSION = "VDBVERSION";
	final String CONN_SCHEMA_VIEW_YN = "SCHEMA_VIEW_YN";
	final String CONN_MAX_SELECT_COUNT = "MAX_SELECT_COUNT";
	final String CONN_MAX_ACTIVE = "MAX_ACTIVE";
	final String CONN_MIN_IDLE = "MIN_IDLE";
	final String CONN_TIMEOUT = "TIMEOUT";
	final String CONN_EXPORTCOUNT = "EXPORTCOUNT";
	final String CONN_BASETABLE_YN = "BASETABLE_YN";
	final String CONN_LAZYLOAD_YN = "LAZYLOAD_YN";
	
	final String DBDRIVER = "DBDRIVER";
	final String VALIDATION_QUERY = "VALIDATION_QUERY";
	
	final String PARAM_UID = "uid";
	final String PARAM_ROLE = "userRole";
	
}
