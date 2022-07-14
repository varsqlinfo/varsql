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
	MYSQL("mysql")
	,DB2("db2")
	,ORACLE("oracle")
	,SQLSERVER("sqlserver", true)
	,MARIADB("mariadb")
	,DERBY("derby")
	,HIVE("hive")
	,HSQLDB("hsqldb")
	,POSTGRESQL("postgresql")
	,INGRES("ingres")
	,H2("h2")
	,TIBERO("tibero")
	,CUBRID("cubrid")
	,SYBASE("sybase")
	,OTHER("other");

	private String dbVenderName;
	
	private boolean useDatabaseName;

	private DBVenderType(String db){
		this(db, false);
	}
	
	private DBVenderType(String db, boolean useDatabaseName){
		this.dbVenderName =db;
		this.useDatabaseName =useDatabaseName;
	}

	public String getDbVenderName() {
		return dbVenderName;
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
}
