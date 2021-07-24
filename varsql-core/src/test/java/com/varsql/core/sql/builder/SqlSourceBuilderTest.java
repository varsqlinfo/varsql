package com.varsql.core.sql.builder;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.varsql.core.db.DBType;
import com.varsql.core.test.BaseTest;

class SqlSourceBuilderTest extends BaseTest {

	@Test
	void testOracle() {
		String sql;
		
		DBType dbType = DBType.ORACLE;; 
		
		try {
			sql = getResourceContent("/query/query.txt");
			
			HashMap param = new HashMap();
			
			List<SqlSource> statements = SqlSourceBuilder.parse(sql,param, dbType);
			
			for(SqlSource statement : statements){
				
				System.out.println("main sysout " + statement.getQuery());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	void testDruid() {
		String sql;
		
		DBType dbType = DBType.ORACLE;; 
		
		try {
			sql = getResourceContent("/query/query.txt");
			
			HashMap param = new HashMap();
			
			List<SQLStatement> statements = SQLUtils.toStatementList(sql, dbType.getDbParser());
			
			for(SQLStatement statement : statements){
				
				System.out.println("main sysout " + SQLUtils.toSQLString(statement, dbType.getDbParser() , null));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
