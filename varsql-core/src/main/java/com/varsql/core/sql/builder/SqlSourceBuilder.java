package com.varsql.core.sql.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.varsql.core.connection.pool.ConnectionValidation;
import com.varsql.core.sql.util.SqlReplaceUtils;
import com.vartech.common.app.beans.ResponseResult;


/**
 * 
 * @FileName  : SqlSourceBuilder.java
 * @프로그램 설명 : source builder
 * @Date      : 2016. 12. 15. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SqlSourceBuilder {
	private static Logger log = LoggerFactory.getLogger(ConnectionValidation.class);
	
	private static String removeClassNameRegular = "^SQL|Statement$|^DB2|^Oracle|^Mysql|^Hive|^Odps|^Phoenix|^PG|^SQLServer|^H2|^Mariadb|^Tibero";
	
	
	private SqlSourceBuilder(){}
	
	public static void main(String[] args) {
		String sql;
		
		String dbType = JdbcConstants.ORACLE; 
		
		try {
			sql = FileUtils.readFileToString(new File("c:/zzz/sqltest.txt"));
			
			HashMap param = new HashMap();
			
			List<SqlSource> statements = SqlSourceBuilder.getSqlSourceList(sql,param, dbType);
			
			for(SqlSource statement : statements){
				
				System.out.println("main sysout " + statement.getQuery());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Method Name  : parse
	 * @Method 설명 : sql 에서 쿼리 분리
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param sql
	 * @return
	 */
	public static List<SqlSource> parse(String query) {
		return parse(query , new HashMap());
	}
	
	public static List<SqlSource> parse(String query, Map<String,String> param) {
		return getSqlSourceList(query, param, null); 
	}
	
	public static List<SqlSource> parse(String query, Map<String,String> param, String dbType) {
		try{
			return getSqlSourceList(query, param, dbType);
		}catch(Exception e){
			log.error("parse ",e);
			
			return getDefaultSqlSource(query, param);
		}
	}
	
	public static ResponseResult parseResponseResult(String query, Map<String,String> param, String dbType) {
		ResponseResult result = new ResponseResult(); 
		try{
			result.setItemList(getSqlSourceList(query, param, dbType));
		}catch(Exception e){
			log.error("parse ",e);
			result.setMessage(e.getMessage());
			result.setItemList(getDefaultSqlSource(query, param));
			
			//result.setItemList(new OldSqlSourceBuilder().parse(query, param));
		}
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : getSqlSource
	 * @Method 설명 : sqlSource 구하기
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param newQuery
	 * @param param 
	 * @return
	 * @throws ParseException 
	 */
	public static SqlSource getSqlSource(String sql) {
		return getSqlSource(sql , new HashMap());
	}
	public static SqlSource getSqlSource(String sql, Map<String, String> param) {
		return getSqlSource(sql , param , null);
	}
	public static SqlSource getSqlSource(String sql, Map<String, String> param, String dbType) {
		return getSqlSourceList(sql , param , dbType).get(0);
	}
	
	private static List<SqlSource> getSqlSourceList(String sql, Map<String, String> param , String dbType) {
		List<SqlSource> queries =new LinkedList<SqlSource>();
		SqlStatement tmpSqlStatement = getSqlStatement(sql, null, true);
		
		List<SQLStatement> statements = SQLUtils.toStatementList(tmpSqlStatement.sql, dbType);
		
		if(statements.size() ==1) {
			queries.add(getSqlSourceBean(statements.get(0), tmpSqlStatement.sql, param));
		}else {
			String tmpQuery = "";
			for(SQLStatement statement : statements){
				tmpQuery = SQLUtils.toSQLString(statement, dbType , null);
				queries.add(getSqlSourceBean(statement, tmpQuery, param));
			}
		}
		
		return queries;
	}
	
	private static SqlSource getSqlSourceBean(SQLStatement statement,String tmpQuery, Map<String, String> param) {
		
		SqlSource sqlSource = new SqlSource();
		
		
		if(statement instanceof SQLSelectStatement){
			sqlSource.setCommandType("SELECT");
		}else if(statement instanceof SQLInsertStatement){
			sqlSource.setCommandType("INSERT");
		}else if(statement instanceof SQLUpdateStatement){
			sqlSource.setCommandType("UPDATE");
		}else if(statement instanceof SQLDeleteStatement){
			sqlSource.setCommandType("DELETE");
		}else if(statement instanceof SQLAlterStatement){
			sqlSource.setCommandType("ALTER");
		}else if(statement instanceof SQLDropStatement){
			sqlSource.setCommandType("DROP");
		}else if(statement instanceof SQLTruncateStatement){
			sqlSource.setCommandType("TRUNCATE");
		}else{
			String simpleName = statement.getClass().getSimpleName();
			sqlSource.setCommandType( simpleName.replaceAll(removeClassNameRegular, ""));
		}
		
		if(statement.isAfterSemi()){
			tmpQuery = tmpQuery.replaceAll(";$","");
		}
		
		if(param != null){
			SqlStatement sqlstate = getSqlStatement(tmpQuery, param, false);
			sqlSource.setQuery(sqlstate.sql);
			sqlSource.setParam(sqlstate.parameter);
		}else{
			sqlSource.setQuery(tmpQuery);
		}
		return sqlSource;
	}

	/**
	 * 
	 * @Method Name  : getSqlStatement
	 * @Method 설명 : 파라미터 변환.
	 * @작성일   : 2017. 8. 1. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param template
	 * @param variables
	 * @return
	 */
	private static SqlStatement getSqlStatement(String sql, Map<String, String> variables, boolean addSingleQuote) {
		
		String regular =SqlReplaceUtils.PARAM_REG;
		
		String dollar =SqlReplaceUtils.PARAM_DOLLAR
				,sharp=SqlReplaceUtils.PARAM_SHARP;
		
		if(!addSingleQuote){
			regular =SqlReplaceUtils.PARAM_REG_SINGLEQUOTE;
			
			dollar = "'"+dollar;
			sharp = "'"+sharp;
		}
		
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(sql);
		
		boolean flag = matcher.find();
		if(flag) {
			StringBuffer buffer = new StringBuffer();
			List paramList = new ArrayList();
			boolean lineCommentStartFlag  = false ,mlineCommentStartFlag  = false;  
			while (flag) {
				String matchGroup  = matcher.group(0);
				String variableKey  = matcher.group(2);
				
				if(matchGroup != null){
					if(matchGroup.startsWith(dollar)) {
						if(addSingleQuote){
							matcher.appendReplacement(buffer,"'"+matchGroup.replace("$","\\$")+"'");
						}else{
							if (variables.containsKey(variableKey)) {
								matcher.appendReplacement(buffer, variables.get(variableKey));
							}else {
								matcher.appendReplacement(buffer, "NULL");
							}
						}
					}else {
						if("/*".equals(matchGroup)) {
							mlineCommentStartFlag = true; 
						}
						if(mlineCommentStartFlag && "*/".equals(matchGroup)) {
							mlineCommentStartFlag = false; 
						}
						if(mlineCommentStartFlag) {
							flag = matcher.find();
							continue; 
						}
						
						if("--".equals(matchGroup)) {
							lineCommentStartFlag = true; 
						}
						if(lineCommentStartFlag && ("\r\n".equals(matchGroup) || "\n".equals(matchGroup)|| "\r".equals(matchGroup))) {
							lineCommentStartFlag = false; 
						}
						
						if(lineCommentStartFlag) {
							flag = matcher.find();
							continue; 
						}
						
						if(matchGroup.startsWith(sharp)) {
							if(addSingleQuote){
								matcher.appendReplacement(buffer, "'"+matchGroup+"'");
							}else{
								if (variables.containsKey(variableKey)) {
									paramList.add(variables.get(variableKey));
								}else {
									paramList.add("");
								}
								matcher.appendReplacement(buffer, "?");
								
							}
						}
					}
				}
				flag = matcher.find();
			}
			matcher.appendTail(buffer);
			
			return new SqlStatement( buffer.toString(), paramList);
			
		}else {
			return new SqlStatement( sql, null);
		}
	}
	
	private static List<SqlSource> getDefaultSqlSource(String query, Map<String, String> param) {
		List<SqlSource> queries =new LinkedList<SqlSource>();
		SqlSource sqlSource;
		sqlSource = new SqlSource();
		sqlSource.setCommandType("OTHER");
			
		if(param != null){
			SqlStatement sqlstate = getSqlStatement(query, param, false);
			sqlSource.setQuery(sqlstate.sql);
			sqlSource.setParam(sqlstate.parameter);
		}else{
			sqlSource.setQuery(query);
		}
		queries.add(sqlSource);
		
		return queries;
	}
}

class SqlStatement{
	public String sql; 
	public List parameter;
	
	SqlStatement(String sql , List parameter){
		this.sql = sql ; 
		this.parameter =parameter;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("sql : ").append(sql)
				.append("\n").append("parameter: ").append(parameter)
				.toString();
	}
}
