package com.varsql.web.app.database;

import com.varsql.db.meta.DBMetaImpl;
import com.varsql.db.meta.DBMetaImplOTHER;
import com.varsql.sql.ddl.script.DDLScript;
import com.varsql.sql.ddl.script.DDLScriptImpl;
import com.varsql.sql.ddl.script.DDLScriptImplOTHER;
import com.varsql.sql.resultset.handle.ResultSetHandle;
import com.varsql.sql.resultset.handle.ResultSetHandleImpl;
import com.varsql.sql.resultset.handle.ResultSetHandleOTHER;

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
	private DDLScript ddlScript;
	private ResultSetHandle resultSetHandle;
	
	private DbTypeEnum(String db){
		try {
			String cls = DBMetaImpl.class.getName()+this.name(); 
			
			this.dbMetaImpl=(DBMetaImpl)Class.forName(cls).newInstance();
		} catch (Exception e) {
			this.dbMetaImpl=new DBMetaImplOTHER();
		}
		
		try {
			String cls = DDLScriptImpl.class.getName()+this.name(); 
			
			
			this.ddlScript=(DDLScriptImpl)Class.forName(cls).newInstance();
		} catch (Exception e) {
			this.ddlScript=new DDLScriptImplOTHER();
		}
		
		try {
			String cls = ResultSetHandleImpl.class.getName()+this.name(); 
			
			this.resultSetHandle=(ResultSetHandle)Class.forName(cls).newInstance();
		} catch (Exception e) {
			this.resultSetHandle=new ResultSetHandleOTHER();
		}
	}
	
	public DBMetaImpl getDBMeta(){
		return this.dbMetaImpl;
	}
	
	public DDLScript getDDLScript(){
		return this.ddlScript;
	}
	
	public ResultSetHandle getResultsetHandle(){
		return this.resultSetHandle;
	}
}
