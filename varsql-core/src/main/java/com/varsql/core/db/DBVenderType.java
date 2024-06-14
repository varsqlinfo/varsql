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
	MYSQL("mysql", "com.mysql.jdbc.Driver"), 
	DB2("db2", "com.ibm.db2.jcc.DB2Driver"),
	ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
	SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", false),
	MARIADB("mariadb", "org.mariadb.jdbc.Driver"), 
	DERBY("derby", "org.apache.derby.jdbc.ClientDriver"),
	HSQLDB("hsqldb", "org.hsqldb.jdbcDriver"), 
	POSTGRESQL("postgresql", "org.postgresql.Driver"),
	H2("h2", "org.h2.Driver"), 
	TIBERO("tibero", "com.tmax.tibero.jdbc.TbDriver"),
	CUBRID("cubrid", "cubrid.jdbc.driver.CUBRIDDriver"), 
	SYBASE("sybase", "com.sybase.jdbc4.jdbc.SybDriver"),
	OTHER("other", null);

	private String dbVenderName;
	
	private String driverClass;
	
	private boolean useDatabaseName;

	
	private DBVenderType(String db, String driverClass){
		this(db, driverClass, false);
	}
	
	private DBVenderType(String db, String driverClass, boolean useDatabaseName){
		this.dbVenderName =db;
		this.driverClass =driverClass;
		this.useDatabaseName =useDatabaseName;
	}

	public String getDbVenderName() {
		return dbVenderName;
	}
	
	public String getName() {
		return name();
	}

	public static DBVenderType getDBType(String db) {
		if(db != null) {
			db = db.toUpperCase();
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
}
