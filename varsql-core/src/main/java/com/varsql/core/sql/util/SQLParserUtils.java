package com.varsql.core.sql.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.pattern.convert.ConvertResult;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMappingUtil;
import com.varsql.core.sql.type.SQLCommandType;

public final class SQLParserUtils {

	final private static SQLParserFeature[] DEFAULT_FEATURES = { SQLParserFeature.KeepComments, SQLParserFeature.SkipComments };
	final private static FormatOption formatOpt = new FormatOption(VisitorFeature.OutputPrettyFormat,VisitorFeature.OutputSkipSelectListCacheString);
	static{
		formatOpt.setUppCase(false);
	}

	private static String removeClassNameRegular = "^SQL|Statement$|^DB2|^Oracle|^Mysql|^Hive|^Odps|^Phoenix|^PG|^SQLServer|^H2|^Mariadb|^Tibero";

	private static ParameterMappingUtil parameterMappingUtil = new ParameterMappingUtil();

	private SQLParserUtils() {}

	public static List<SqlSource> getSqlSourceList(String sql, Map<String, String> param , DBVenderType dbType) {
		List<SqlSource> queries =new LinkedList<SqlSource>();

		com.alibaba.druid.DbType parser = getDbParser(dbType);

		List<SQLStatement> statements = SQLUtils.parseStatements(sql, parser, DEFAULT_FEATURES);

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

	private static SqlSource getSqlSourceBean(SQLStatement statement,String tmpQuery, Map<String, String> param, DBVenderType dbType) {

		SqlSource sqlSource = new SqlSource();
		sqlSource.setOrginSqlParam(param);
		
		if(statement instanceof SQLSelectStatement){
			sqlSource.setCommand(SQLCommandType.SELECT);
		}else if(statement instanceof SQLInsertStatement){
			sqlSource.setCommand(SQLCommandType.INSERT);
		}else if(statement instanceof SQLUpdateStatement){
			sqlSource.setCommand(SQLCommandType.UPDATE);
		}else if(statement instanceof SQLDeleteStatement){
			sqlSource.setCommand(SQLCommandType.DELETE);
		}else if(statement instanceof SQLAlterStatement){
			sqlSource.setCommand(SQLCommandType.ALTER);
		}else if(statement instanceof SQLDropStatement){
			sqlSource.setCommand(SQLCommandType.DROP);
		}else if(statement instanceof SQLTruncateStatement){
			sqlSource.setCommand(SQLCommandType.TRUNCATE);
		}else if(statement instanceof SQLCallStatement){
			sqlSource.setCommand(SQLCommandType.CALL);
		}else if(statement instanceof SQLCreateStatement){
			sqlSource.setCommand(SQLCommandType.CREATE);
		}else if(statement instanceof SQLMergeStatement){
			sqlSource.setCommand(SQLCommandType.MERGE);
		}else if(statement instanceof SQLWithSubqueryClause){
			sqlSource.setCommand(SQLCommandType.WITH_SUBQUERY);
		}else{
			String simpleName = statement.getClass().getSimpleName();
			sqlSource.setCommand(SQLCommandType.getSQLCommandType(simpleName.replaceAll(removeClassNameRegular, "")));
		}

		tmpQuery = MetaControlFactory.getDbInstanceFactory(dbType).getCommandTypeFactory().getCommandType(sqlSource.getCommand()).checkSql(tmpQuery);

		if(param != null){
			SqlStatement sqlstate = getSqlStatement(tmpQuery, param, false, dbType);
			sqlSource.setQuery(sqlstate.sql);
			sqlSource.setParamList(sqlstate.parameter);
		}else{
			sqlSource.setQuery(tmpQuery);
		}
		return sqlSource;
	}

	@SuppressWarnings("unchecked")
	private static SqlStatement getSqlStatement(String sql, Map<String, String> variables, boolean addSingleQuote, DBVenderType dbType) {

		ConvertResult convertResult = parameterMappingUtil.sqlParameter(dbType, sql, variables);

		if(convertResult.getParameterInfo().size() > 0) {
			return new SqlStatement(convertResult.getCont(), (List<ParameterMapping>)convertResult.getParameterInfo());
		}else {
			return new SqlStatement(convertResult.getCont(), null);
		}
	}

	public static com.alibaba.druid.DbType getDbParser(DBVenderType dbType) {
		com.alibaba.druid.DbType val = com.alibaba.druid.DbType.of(dbType.getDbVenderName());
		return val==null ? com.alibaba.druid.DbType.mariadb :val;
	}

	public static String getParserString(String sql ,DBVenderType dbType) {

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
		return getDefaultSqlSource(query, param, DBVenderType.OTHER);
	}

	public static List<SqlSource> getDefaultSqlSource(String query, Map<String, String> param, DBVenderType dbType) {
		List<SqlSource> queries =new LinkedList<SqlSource>();
		SqlSource sqlSource = new SqlSource();
		sqlSource.setCommand(SQLCommandType.OTHER);
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