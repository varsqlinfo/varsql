package com.varsql.web.app.database.db2;

import com.varsql.db.meta.DBMeta;
import com.varsql.db.meta.DBMetaImpl;
import com.varsql.db.meta.DBMetaImplDB2;
import com.varsql.db.meta.DBMetaImplMYSQL;
import com.varsql.db.meta.DBMetaImplOTHER;

public enum DbTypeEnum {
	MYSQL("mysql")
	,DB2("db2")
	,ORACLE("oracle")
	,MSSQL("mssql")
	,MARIADB("mariadb")
	,DERBY("derby")
	,HSQLDB("hsqldb")
	,POSTGRESQL("postgresql")
	,INGRES("ingres")
	,H2("h2")
	,FIREBIRD("firebird")
	,OTHER("other");
	
	private DBMetaImpl dbMetaImpl;
	
	private DbTypeEnum(String db){
		try {
			String cls = DBMetaImpl.class.getName()+this.name(); 
			this.dbMetaImpl=(DBMetaImpl)Class.forName(cls).newInstance();
		} catch (Exception e) {
			this.dbMetaImpl=new DBMetaImplOTHER();
		}
	}
	
	public DBMetaImpl getDBMetaImpl(){
		return this.dbMetaImpl;
	}
}
