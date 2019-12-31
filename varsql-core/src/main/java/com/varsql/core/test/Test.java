package com.varsql.core.test;

import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.DBType;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.varsql.core.db.beans.ddl.DDLCreateOption;
import com.varsql.core.db.ddl.template.DDLTemplateFactory;
import com.varsql.core.db.meta.DBMetaImplOTHER;
import com.varsql.core.exception.ConnectionException;
import com.varsql.core.sql.util.SQLUtil;

public class Test {
	public static void main(String[] args) {
		
		Map param = new HashMap();
		
		param.put("ddlOption", new DDLCreateOption());
		
		System.out.println(DDLTemplateFactory.getInstance().ddlRender(DBType.ORACLE.getDbVenderName(), "sequenceScript", param));
		
		//System.out.println(MetaControlBean.ORACLE.getResultSetMetaHandlerImpl());
//		String dbObjType = "table";
//		String callMethodName =String.format("get%ss", StringUtil.capitalize(dbObjType));
//		
//		System.out.println("callMethodName : "+callMethodName);
//		
		//if(true) return ; 
		//new Test().execute();
	}
	
	private void execute() {
		System.setProperty("com.varsql.install.root", "E:/91.gitvar/sources/varsql-java-core/resource");
		//System.setProperty("com.varsql.config.file", "C:/01.HKMC/04.source/varsql-config/resource/etc/varsqlConfig.properties");
		System.out.println(Configuration.getInstance().getProperties());
		
		try {
			ResultSet rs =null;
			Connection conn =null;
			
			PreparedStatement pstmt =null; 
			try {
				
				DatabaseParamInfo databaseParamInfo = new DatabaseParamInfo();
				
				String [] aaa = {""}; 
				databaseParamInfo.setObjectType("table");
				databaseParamInfo.setSchema("PUBLIC");
				databaseParamInfo.setType("DB2");
				databaseParamInfo.setObjectName("VTCONNECTION");
				
				DBMetaImplOTHER bbb = new DBMetaImplOTHER(null); 
				
				String methodName =  "getTableMetadata"; 
				
				
				
				
				System.out.println("method1 : "+ ReflectionUtils.findField(bbb.getClass(), methodName));
				
				if(true) return ; 
				
				//CompanyInfo ci= (CompanyInfo)SQLUtil.resultsetForList(rs , CompanyInfo.class, true);
				
				//System.out.println(SQLUtil.resultsetForList(rs));
				
				System.out.println(SQLUtil.resultsetForList(rs, CompanyInfo.class));
				//System.out.println(ci);
				//System.out.println(ci.getCompany_name());
				
				//ArrayList result= getResultData1(rs);
				
				//System.out.println("result : " + result.size());
				
			} catch (ConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				SQLUtil.close(conn, pstmt, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ArrayList getResultData1(ResultSet rs) throws Exception {
		if (rs == null) {
			return new ArrayList();
		}
		ArrayList rows = new ArrayList();
		try {
			ResultSetMetaData rsmd = null;
			rsmd = rs.getMetaData();

			int count = rsmd.getColumnCount();
			String[] columns_key = new String[count];

			for (int i = 1; i <= count; i++) {
				columns_key[(i - 1)] = rsmd.getColumnName(i);
			}

			HashMap columns = null;
			Reader input = null;
			char[] buffer = null;
			int byteRead = -1;
			Object colObj = null;
			String colNm = null;
			while (rs.next()) {
				columns = new HashMap(count);
				for (int i = 0; i < count; i++) {
					colNm = columns_key[i];
					colObj = rs.getObject(colNm);

					if ((colObj instanceof Clob))
						try {
							StringBuffer output = new StringBuffer();
							input = rs.getCharacterStream(colNm);
							buffer = new char[1024];
							while ((byteRead = input.read(buffer, 0, 1024)) != -1) {
								output.append(buffer, 0, byteRead);
							}
							input.close();
							columns.put(colNm.toLowerCase(), output.toString());
						} catch (Exception e) {
							throw e;
						}
					else if ((colObj instanceof Timestamp))
						columns.put(colNm.toLowerCase(), colObj);
					else if ((colObj instanceof Date))
						columns.put(colNm.toLowerCase(), colObj);
					else {
						columns.put(colNm.toLowerCase(), colObj);
					}
				}
				rows.add(columns);
			}

			return rows;
		} catch (SQLException e1) {
			throw e1;
		} catch (Exception e) {
			throw e;
		}
	}
}
