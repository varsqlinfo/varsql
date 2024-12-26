package com.varsql.core.db;

/**
 *
 * @FileName  : DBVenderType.java
 * @프로그램 설명 : db type
 * @Date      : 2019. 11. 26.
 * @작성자      : ytkim
 * @변경이력 :
 */
public enum DBVenderType {
	MYSQL("mysql", "com.mysql.jdbc.Driver",false), 
	DB2("db2", "com.ibm.db2.jcc.DB2Driver",false),
	ORACLE("oracle", "oracle.jdbc.driver.OracleDriver",false, "xe"),
	SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", true),
	MARIADB("mariadb", "org.mariadb.jdbc.Driver",false), 
	DERBY("derby", "org.apache.derby.jdbc.ClientDriver",false),
	HSQLDB("hsqldb", "org.hsqldb.jdbcDriver",false), 
	POSTGRESQL("postgresql", "org.postgresql.Driver",false),
	H2("h2", "org.h2.Driver",false), 
	TIBERO("tibero", "com.tmax.tibero.jdbc.TbDriver",false),
	CUBRID("cubrid", "cubrid.jdbc.driver.CUBRIDDriver",false), 
	SYBASE("sybase", "com.sybase.jdbc4.jdbc.SybDriver",false),
	OTHER("other", null, false);

	private String dbVenderName;
	
	private String driverClass;
	
	private boolean useDatabaseName;

	private String defaultDatabaseName;
	
	private DBVenderType(String db, String driverClass, boolean useDatabaseName){
		this(db, driverClass, useDatabaseName, "");
	}
	
	private DBVenderType(String db, String driverClass, boolean useDatabaseName, String defaultDatabaseName){
		this.dbVenderName =db;
		this.driverClass =driverClass;
		this.useDatabaseName =useDatabaseName;
		this.defaultDatabaseName =defaultDatabaseName;
	}
	
	public String getDbVenderName() {
		return dbVenderName;
	}
	
	public String getName() {
		return name();
	}

	public static DBVenderType getDBType(String db) {
		if(db != null) {
			for (DBVenderType dbType : values()) {
				if(db.equalsIgnoreCase(dbType.name())) {
					return dbType;
				}
			}
		}
		return DBVenderType.OTHER;
	}

	public boolean equalsName(String type) {
		type = type.toUpperCase();
		for (DBVenderType dbType : values()) {
			if(dbType.name().equals(type)) {
				return true;
			}
		}

		return false;
	}

	public boolean isUseDatabaseName() {
		return useDatabaseName;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getDefaultDatabaseName() {
		return defaultDatabaseName;
	}
}
