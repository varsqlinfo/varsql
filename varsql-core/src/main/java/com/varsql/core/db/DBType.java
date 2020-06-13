package com.varsql.core.db;

import com.alibaba.druid.util.JdbcConstants;

/**
 *
 * @FileName  : DBType.java
 * @프로그램 설명 : db type
 * @Date      : 2019. 11. 26.
 * @작성자      : ytkim
 * @변경이력 :
 */
public enum DBType {
	MYSQL("mysql" ,JdbcConstants.MYSQL)
	,DB2("db2",JdbcConstants.DB2)
	,ORACLE("oracle",JdbcConstants.ORACLE)
	,MSSQL("mssql",JdbcConstants.SQL_SERVER)
	,MARIADB("mariadb",JdbcConstants.MARIADB)
	,DERBY("derby",JdbcConstants.DERBY)
	,HIVE("hive",JdbcConstants.HIVE)
	,HSQLDB("hsqldb",JdbcConstants.HSQL)
	,POSTGRESQL("postgresql",JdbcConstants.POSTGRESQL)
	,INGRES("ingres",JdbcConstants.MARIADB)
	,H2("h2",JdbcConstants.H2)
	,TIBERO("tibero",JdbcConstants.ORACLE)
	,CUBRID("cubrid",JdbcConstants.MARIADB)
	,SYBASE("sybase",JdbcConstants.SYBASE)
	,OTHER("other",JdbcConstants.MARIADB);

	private String dbVenderName;
	private String dbParser;

	private DBType(String db, String dbParserPrefix){
		this.dbVenderName =db;
		this.dbParser =dbParserPrefix;
	}

	public String getDbVenderName() {
		return dbVenderName;
	}

	public String getDbParser() {
		return dbParser;
	}

	public static String getDbParser(String db) {
		if(db != null) {
			db = db.toUpperCase();
			for (DBType dbType : values()) {
				if(db.equalsIgnoreCase(dbType.name())) {
					return dbType.dbParser;
				}
			}
		}
		return DBType.OTHER.dbParser;
	}

	public static DBType getDBType(String db) {
		if(db != null) {
			db = db.toUpperCase();
			for (DBType dbType : values()) {
				if(db.equalsIgnoreCase(dbType.name())) {
					return dbType;
				}
			}
		}
		return DBType.OTHER;
	}

	public boolean equals(String type) {
		type = type.toUpperCase();
		for (DBType dbType : values()) {
			if(dbType.name().equals(type)) {
				return true;
			}
		}

		return false;
	}
}
