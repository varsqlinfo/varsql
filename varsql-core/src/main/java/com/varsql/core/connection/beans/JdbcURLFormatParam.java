package com.varsql.core.connection.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JdbcURLFormatParam {
	
	private String type;
	
	private String serverIp;

	private int port;

	private String databaseName;

	private String optStr;
	
	
}
