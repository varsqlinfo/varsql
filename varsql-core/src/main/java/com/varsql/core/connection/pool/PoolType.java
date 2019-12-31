package com.varsql.core.connection.pool;

public enum PoolType {
	HIKARI("hikari") 
	, DBCP2("dbcp2") 
	//,DBCP("dbcp");
	;
	
	ConnectionPoolInterface poolInterface;
	
	PoolType(String type){
		if("dbcp2".equals(type)){
			poolInterface = new ConnectionDBCP2();
		}else if("hikari".equals(type)){
			poolInterface = new ConnectionHIKARI();
		}else {
			poolInterface = new ConnectionDBCP2();
		}
	}
	
	public ConnectionPoolInterface getPoolBean (){
		return poolInterface;
	}
	
}
