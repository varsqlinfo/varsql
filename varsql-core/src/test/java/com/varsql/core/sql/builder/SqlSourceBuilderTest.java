package com.varsql.core.sql.builder;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.alibaba.druid.util.JdbcConstants;
import com.varsql.core.test.BaseTest;

class SqlSourceBuilderTest extends BaseTest {

	@Test
	void testOracle() {
		String sql;
		
		String dbType = JdbcConstants.ORACLE; 
		
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

}
