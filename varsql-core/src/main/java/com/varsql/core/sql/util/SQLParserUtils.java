package com.varsql.core.sql.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.varsql.core.db.DBType;
import com.varsql.core.pattern.convert.ConvertResult;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMappingUtil;

public final class SQLParserUtils {

	final private static SQLParserFeature[] DEFAULT_FEATURES = { SQLParserFeature.KeepComments, SQLParserFeature.SkipComments };
	final private static FormatOption formatOpt = new FormatOption(VisitorFeature.OutputPrettyFormat,VisitorFeature.OutputSkipSelectListCacheString);
	static{
		formatOpt.setUppCase(false);
	}

	private static String removeClassNameRegular = "^SQL|Statement$|^DB2|^Oracle|^Mysql|^Hive|^Odps|^Phoenix|^PG|^SQLServer|^H2|^Mariadb|^Tibero";

	private static ParameterMappingUtil parameterMappingUtil = new ParameterMappingUtil();


	private SQLParserUtils() {}

	public static List<SqlSource> getSqlSourceList(String sql, Map<String, String> param , DBType dbType) {
		List<SqlSource> queries =new LinkedList<SqlSource>();

		com.alibaba.druid.DbType parser = getDbParser(dbType);

		List<SQLStatement> statements = SQLUtils.toStatementList(sql, parser);

		if(statements.size() ==1) {
			queries.add(getSqlSourceBean(statements.get(0), sql, param, dbType));
		}else {
			String tmpQuery = "";
			for(SQLStatement statement : statements){
				tmpQuery = SQLUtils.toSQLString(statement, parser, null);
				queries.add(getSqlSourceBean(statement, tmpQuery, param, dbType));
			}
		}

		return queries;
	}

	private static SqlSource getSqlSourceBean(SQLStatement statement,String tmpQuery, Map<String, String> param, DBType dbType) {

		SqlSource sqlSource = new SqlSource();
		sqlSource.setOrginSqlParam(param);

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
			SqlStatement sqlstate = getSqlStatement(tmpQuery, param, false, dbType);
			sqlSource.setQuery(sqlstate.sql);
			sqlSource.setParamList(sqlstate.parameter);
		}else{
			sqlSource.setQuery(tmpQuery);
		}
		return sqlSource;
	}

	private static SqlStatement getSqlStatement(String sql, Map<String, String> variables, boolean addSingleQuote, DBType dbType) {

		ConvertResult convertResult = parameterMappingUtil.sqlParameter(dbType, sql, variables);

		if(convertResult.getParameterInfo().size() > 0) {
			return new SqlStatement(convertResult.getCont(), (List<ParameterMapping>)convertResult.getParameterInfo());
		}else {
			return new SqlStatement(convertResult.getCont(), null);
		}
	}

	public static com.alibaba.druid.DbType getDbParser(DBType dbType) {
		com.alibaba.druid.DbType val = com.alibaba.druid.DbType.of(dbType.getDbVenderName());
		return val==null ? com.alibaba.druid.DbType.other :val;
	}

	public static String getParserString(String sql ,DBType dbType) {

		SQLStatementParser parser = com.alibaba.druid.sql.parser.SQLParserUtils.createSQLStatementParser(sql, getDbParser(dbType), DEFAULT_FEATURES);

		parser.setKeepComments(true);
		List<SQLStatement> statementList = parser.parseStatementList();

		return SQLUtils.toSQLString(statementList, getDbParser(dbType), null, formatOpt);

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
	public static List<SqlSource> getDefaultSqlSource(String query, Map<String, String> param) {
		return getDefaultSqlSource(query, param, DBType.OTHER);
	}

	public static List<SqlSource> getDefaultSqlSource(String query, Map<String, String> param, DBType dbType) {
		List<SqlSource> queries =new LinkedList<SqlSource>();
		SqlSource sqlSource = new SqlSource();
		sqlSource.setCommandType("OTHER");
		sqlSource.setOrginSqlParam(param);

		if(param != null){
			SqlStatement sqlstate = getSqlStatement(query, param, false, dbType);
			sqlSource.setQuery(sqlstate.sql);
			sqlSource.setParamList(sqlstate.parameter);
		}else{
			sqlSource.setQuery(query);
		}
		queries.add(sqlSource);

		return queries;
	}
}

class SqlStatement{
	public String sql;
	public List<ParameterMapping> parameter;

	SqlStatement(String sql , List<ParameterMapping> parameter){
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