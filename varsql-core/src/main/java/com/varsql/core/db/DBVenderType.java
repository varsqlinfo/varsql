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
	MYSQL("mysql", "com.mysql.jdbc.Driver", false, 3306),
    DB2("db2", "com.ibm.db2.jcc.DB2Driver", false, 50000),
    ORACLE("oracle", "oracle.jdbc.driver.OracleDriver", false, 1521, "xe"),
    SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", true, 1433),
    MARIADB("mariadb", "org.mariadb.jdbc.Driver", false, 3306),
    DERBY("derby", "org.apache.derby.jdbc.ClientDriver", false, 1527),
    HSQLDB("hsqldb", "org.hsqldb.jdbcDriver", false, 9001),
    POSTGRESQL("postgresql", "org.postgresql.Driver", false, 5432),
    H2("h2", "org.h2.Driver", false, 9092),
    TIBERO("tibero", "com.tmax.tibero.jdbc.TbDriver", false, 8629),
    CUBRID("cubrid", "cubrid.jdbc.driver.CUBRIDDriver", false, 33000),
    SYBASE("sybase", "com.sybase.jdbc4.jdbc.SybDriver",false, 5000),
	OTHER("other", null, false, -1);
	
	private String dbVenderName;
	
	private String driverClass;
	
	private boolean useDatabaseName;

	private String defaultDatabaseName;
	
	private int defaultPort;
	
	private DBVenderType(String db, String driverClass, boolean useDatabaseName, int defaultPort){
		this(db, driverClass, useDatabaseName, defaultPort,  "");
	}
	
	private DBVenderType(String db, String driverClass, boolean useDatabaseName, int defaultPort, String defaultDatabaseName){
		this.dbVenderName =db;
		this.driverClass =driverClass;
		this.useDatabaseName =useDatabaseName;
		this.defaultPort =defaultPort;
		this.defaultDatabaseName =defaultDatabaseName;
	}
	
	public String getDbVenderName() {
		return dbVenderName;
	}
	
	public String getName() {
		return name();
	}
	
	public int getDefaultPort() {
		return defaultPort;
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
}
