package com.varsql.core.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.util.StringUtil;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.resultset.handler.ResultSetHandler;
import com.varsql.core.sql.util.SQLUtil;

public class DbDataTest {
	
	private DBConnectionInfo selectDbType = DBConnectionInfo.TEBERO;
	
	public static void main(String[] args) throws Exception {
		DbDataTest aaa = new  DbDataTest();
		
		aaa.getTableDataCheck();
	}
	
	private void getTableDataCheck() throws Exception {
		Connection connection = null;
		PreparedStatement pstat = null; 
		ResultSet resultSet = null; 
		try {
			connection = getConnection();
	
			pstat = connection.prepareStatement("select * from " + selectDbType.getTable());
			
			pstat.setMaxRows(10);
			
			resultSet = pstat.executeQuery(); 
			
			ResultSetMetaData rsmd = resultSet.getMetaData();
			
			int count = rsmd.getColumnCount();
			String [] columns_key = new String[count];
			String [] columns_type = new String[count];
			String [] column_type_name = new String[count];
			
			String columnName = "", columnTypeName="", columnTypeStr="";
			int columnType;
			
			for (int i = 0 ; i < count; i++) {
				int idx =i+1;
				columnName= rsmd.getColumnName(idx);
				columnType = rsmd.getColumnType(idx);
				columnTypeName = rsmd.getColumnTypeName(idx);
				
				columns_key[i] = columnName;
				columns_type[i] =columnType+"";
				column_type_name[i] = columnTypeName;
			}
			
			
			while (resultSet.next()) {
				for (int colIdx = 0; colIdx < count; colIdx++) {				
					columnName = columns_key[colIdx];
					columnTypeStr = columns_type[colIdx];
					columnTypeName = column_type_name[colIdx];
					
					Object obj = resultSet.getObject(columnName);
					Class cls =null;
					if(obj != null) {
						cls = obj.getClass();
					}else {
						
					}
					
					System.out.println("Column name: [" + columnName +"] ;;type: [" + columnTypeName + "] ;;columnTypeStr: [" + columnTypeStr+ "] ;;cls : [" + cls + "] ;;val : "+obj);
					
				}
	
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(connection, pstat, resultSet);
		}
	}
	private void getColumnCheck() throws Exception {
		Connection connection = null;
		ResultSet resultSet = null; 
		try {
			connection = getConnection();
	
			DatabaseMetaData metadata = connection.getMetaData();
			resultSet = metadata.getColumns(null, null, "T_COMM_CONT_FORM2", null);
	
			while (resultSet.next()) {
				String name = resultSet.getString("COLUMN_NAME");
				String type = resultSet.getString("TYPE_NAME");
				Object data_type = resultSet.getObject("DATA_TYPE");
				String COLUMN_DEF = resultSet.getString(MetaColumnConstants.COLUMN_DEF);
				int size = resultSet.getInt("COLUMN_SIZE");
				String degitsLen = StringUtil.nullToString(resultSet.getString(MetaColumnConstants.DECIMAL_DIGITS));
	
				if (StringUtil.nullToString(COLUMN_DEF) != null && !"".equals(StringUtil.nullToString(COLUMN_DEF))) {
					System.out.println("111111111111");
				}
	
				System.out.println(data_type + "Column name: [" + name + "]; type: [" + type + "]; size: [" + size
						+ "] datatype : " + data_type);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(connection, null, resultSet);
		}
	}

	private Connection getConnection() throws Exception {
		return getConnection(selectDbType);
	}
	
	private Connection getConnection(DBConnectionInfo dbType) throws Exception {
		Class.forName(dbType.getDriver());
		
		System.out.println(dbType.toString());
		
		return DriverManager.getConnection(dbType.getUrl(), dbType.getUsername(), dbType.getPassword());
	}

	enum DBConnectionInfo{
		ORACLE("weblogic","welcome1","dual","jdbc:oracle:thin:@192.168.56.101:1521:orcl","oracle.jdbc.driver.OracleDriver")
		, TEBERO("sys","passw0rd" ,"AQ$_QUEUES", "jdbc:tibero:thin:@tibero.varsqldb.com:8629:tibero","com.tmax.tibero.jdbc.TbDriver");
		
		
		private String driver;

		private String url;

		private String username;

		private String password;
		
		private String table;
		
		DBConnectionInfo(String username , String pw, String table , String url,String driver){
			this.driver = driver; 
			this.url = url; 
			this.username = username; 
			this.password = pw; 
			this.table = table; 
		}

		public String getDriver() {
			return driver;
		}
		
		public String getUrl() {
			return this.url;
		}
		
		public String getUsername() {
			return this.username;
		}
		
		public String getPassword() {
			return this.password;
		}
		
		public String getTable() {
			return this.table;
		}
		
		@Override
		public String toString() {
			
			return new StringBuilder().append("driver : ").append(driver)
					.append(" ;url : ").append(url)
					.append(" ;username : ").append(username)
					.append(" ;password : ").append(password)
					.append(" ;table : ").append(table)
					.toString()
					;
		}

	}
}
