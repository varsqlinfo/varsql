package com.varsql.core.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBType;
import com.varsql.core.db.valueobject.DatabaseInfo;

public class BaseTest {
	public final static String TEST_CON_UID = "varsql_test_conid"; 
	public String getResourceContent(String filePath) {
		try {
			return FileUtils.readFileToString(new File(getClass().getResource(filePath).getFile()) ,Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ConnectionInfo getBaseConnection(){
		ConnectionInfo connInfo  = new ConnectionInfo();

		connInfo.setConnid(TEST_CON_UID);

		connInfo.setAliasName(TEST_CON_UID);
		
		connInfo.setType(DBType.H2.name());
		connInfo.setDriver("org.h2.Driver");
		connInfo.setUrl("jdbc:h2:file:C:/zzz/resources/varsql");
		connInfo.setUsername("sa");
		connInfo.setPassword("sa");
		
		connInfo.setValidation_query("select 1");
		connInfo.setPool_opt("max_idle=5;min_idle=5;");
		connInfo.setConnection_opt("sslConnection=true");
		connInfo.setTimebetweenevictionrunsmillis(60000);
		connInfo.setTest_while_idle("true");

		connInfo.setMax_active(2);
		connInfo.setMin_idle(1);
		try {
			ConnectionFactory.getInstance().createPool(connInfo);
		}catch(Throwable e) {
			throw new RuntimeException(e);
		}
		return connInfo; 
	}
	
	public DatabaseInfo getDatabaseInfo(ConnectionInfo connInfo) {
		return  new DatabaseInfo(TEST_CON_UID, TEST_CON_UID , connInfo.getType(), connInfo.getAliasName(), ""	, "Y","N",1, "Y", -1);	
	}
}
