package com.varsql.core.sql.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.connection.pool.ConnectionValidation;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.sql.util.SQLParserUtils;
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

	private SqlSourceBuilder(){}

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
		return parse(query, param, DBVenderType.OTHER);
	}

	public static List<SqlSource> parse(String query, Map<String,String> param, DBVenderType dbType) {
		try{
			return SQLParserUtils.getSqlSourceList(query, param, dbType);
		}catch(Exception e){
			log.error("parse ",e);

			try {
				return SQLParserUtils.getSqlSourceList(query, param, DBVenderType.OTHER);
			}catch(Exception e1) {
				return SQLParserUtils.getDefaultSqlSource(query, param, dbType);
			}
		}
	}

	public static ResponseResult parseResponseResult(String query, Map<String,String> param, DBVenderType dbType) {
		ResponseResult result = new ResponseResult();
		try{
			result.setItemList(parse(query, param, dbType));
		}catch(Exception e){
			log.error("parse ",e);
			result.setMessage(e.getMessage());
			result.setItemList(SQLParserUtils.getDefaultSqlSource(query, param));

			//result.setItemList(new OldSqlSourceBuilder().parse(query, param));
		}
		return result;
	}
	/**
	 * @method  : getSqlSource
	 * @desc : sqlSource 구하기
	 * @author   : ytkim
	 * @date   : 2015. 4. 9.
	 * @param sql
	 * @return
	 */
	public static SqlSource getSqlSource(String sql) {
		return getSqlSource(sql , new HashMap());
	}
	public static SqlSource getSqlSource(String sql, Map<String, String> param) {
		return getSqlSource(sql , param , DBVenderType.OTHER);
	}
	public static SqlSource getSqlSource(String sql, Map<String, String> param, DBVenderType dbType) {
		return SQLParserUtils.getSqlSourceList(sql , param , dbType).get(0);
	}

}