package com.varsql.core.pattern.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;
import com.varsql.core.test.BaseTest;

public class SQLCommentRemoveConvertTest extends BaseTest{
	
	private boolean allLogView = false; 
	DBType logViewType = null;//CommentRemoveUtil.CommentType.JAVASCRIPT;
	@Test
	public void testORACLE() {
		String cont = getResourceContent("/comment/sql/oracle.txt");
		
		SQLCommentRemoveConvert cru = new SQLCommentRemoveConvert();
		
		String result = cru.convert(cont, DBType.ORACLE);
		viewResult(result, DBType.ORACLE);
		
		assertNotEquals(result.indexOf("/*"), -10);
		assertNotEquals(result.indexOf("--"), -10);
		
	}
	
	@Test
	public void testMYSQL() {
		String cont = getResourceContent("/comment/sql/mysql.txt");
		SQLCommentRemoveConvert cru = new SQLCommentRemoveConvert();
		String result = cru.convert(cont, DBType.MYSQL);
		
		viewResult(result, DBType.MYSQL);
		
		assertNotEquals(result.indexOf("#"), -10);
		assertNotEquals(result.indexOf("/*"), -10);
	}
	
	@Test
	public void testMARIADB() {
		String cont = getResourceContent("/comment/sql/mariadb.txt");
		SQLCommentRemoveConvert cru = new SQLCommentRemoveConvert();
		String result = cru.convert(cont, DBType.MARIADB);
		
		viewResult(result, DBType.MARIADB);
		
		assertNotEquals(result.indexOf("--"), -10);
		assertNotEquals(result.indexOf("#"), -10);
		assertNotEquals(result.indexOf("/*"), -10);
	}
	
	@Test
	public void testMSSQL(){
		String cont = getResourceContent("/comment/sql/mssql.txt");
		SQLCommentRemoveConvert cru = new SQLCommentRemoveConvert();
		String result = cru.convert(cont, DBType.MSSQL);
		
		viewResult(result, DBType.MSSQL);
		
		assertFalse(result.indexOf("/*") > -1, result+ "\n // multi line comment not remove");
		assertFalse(result.indexOf("--") > -1);
	}
	
	@Test
	public void testPOSTGRESQL(){
		String cont = getResourceContent("/comment/sql/postgresql.txt");
		SQLCommentRemoveConvert cru = new SQLCommentRemoveConvert();
		String result = cru.convert(cont, DBType.POSTGRESQL);
		
		viewResult(result, DBType.POSTGRESQL);
		
		assertNotEquals(result.indexOf("/*"), -10);
	}
	
	
	private void viewResult(String result, DBType property) {
		
		if(allLogView) {
			System.out.println(result);
		}else {
			if(property.equals(logViewType)) {
				System.out.println(result);
			}
		}
		
	}
}
