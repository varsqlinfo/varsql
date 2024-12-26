package com.varsql.core.sql.util;

import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.BatchException;
import com.varsql.core.sql.SqlExecuteManager;
import com.varsql.core.sql.StatementSetter;
import com.varsql.core.sql.beans.ExecuteStatementInfo;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.beans.GenQueryInfo;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.beans.SqlExecuteBean;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.builder.VarsqlStatementType;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMode;
import com.varsql.core.sql.type.SQLCommandType;
import com.varsql.core.sql.type.SQLDataType;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

/**
 *
 * @FileName : SQLUtils.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql query utils
 * @변경이력	:
 */
public final class SQLUtils {

	private SQLUtils(){};

	public static String escapeValue(Object val) {
		if(val==null) return null;

		String s = val.toString();

		StringBuffer sb = new StringBuffer();
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '\'':
				sb.append("''");
				break;
			case '\\':
				sb.append("\\");
				break;
			case '\b':
				sb.append("\b");
				break;
			case '\f':
				sb.append("\f");
				break;
			case '\n':
				sb.append("\n");
				break;
			case '\r':
				sb.append("\r");
				break;
			case '\t':
				sb.append("\t");
				break;
			case '/':
				sb.append("/");
				break;
			default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	public static String generateSelectQuery(String tblName, String conditionQuery, DBVenderType dbType) {

		StringBuilder reqQuerySb = new StringBuilder().append("select * from ").append(tblName).append(" where 1=1 ");

		if (!StringUtils.isBlank(conditionQuery)) {
			conditionQuery = StringUtils.lTrim(conditionQuery).toLowerCase();
			if (conditionQuery.startsWith("where")) {
				conditionQuery = conditionQuery.replaceFirst("where", "");
			}

			if (conditionQuery.startsWith("and")) {
				conditionQuery = conditionQuery.replaceFirst("and", "");
			}

			conditionQuery = " and " + conditionQuery;
			reqQuerySb.append(conditionQuery);
		}

		return reqQuerySb.toString();
	}
	
	public static GenQueryInfo generateInsertQuery(String tableName, List<ExportColumnInfo> columns, DBVenderType dbType) {
		return generateInsertQuery(tableName, columns, MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl());
	}
	
	public static GenQueryInfo generateInsertQuery(String tableName, List<ExportColumnInfo> columns, DataTypeFactory dataTypeFactory) {
		
		StringBuffer querySb = new StringBuffer();

		querySb.append("insert into ").append(tableName).append(" (");

		StringBuilder paramSb =new StringBuilder();
		boolean firstFlag = true;
		
		
		List<ExportColumnInfo> newColumnList = new LinkedList<ExportColumnInfo>();
		for (ExportColumnInfo eci : columns) {
			
			if(dataTypeFactory.getDataType(eci.getTypeCode(), eci.getType()).isExcludeImportColumn()) {
				continue;
			}
			
			newColumnList.add(eci);
			if(firstFlag) {
				querySb.append(eci.getName());
				paramSb.append("?");
				firstFlag = false;
			}else {
				querySb.append(", ").append(eci.getName());
				paramSb.append(", ?");
			}
		}

		querySb.append(") values ( ").append(paramSb.toString()).append(") ") ;
		
		return new GenQueryInfo(querySb.toString(), newColumnList);
	}
	
	/**
	 * truncate 쿼리
	 * 
	 * @param tableName
	 * @param dataTypeFactory
	 * @return
	 */
	public static String generateTruncateQuery(String tableName, DataTypeFactory dataTypeFactory) {
		return String.format("truncate table %s", tableName);
	}
	
	/**
	 * 삭제 쿼리
	 * 
	 * @param tableName
	 * @param dataTypeFactory
	 * @return
	 */
	public static String generateDeleteQuery(String tableName, DataTypeFactory dataTypeFactory) {
		return String.format("delete from %s", tableName);
	}
	
	/**
	 * 
	 * @method  : getSqlExecute
	 * @desc :
	 * @author      : ytkim
	 * @param sqlExecuteInfo
	 * @param conn
	 * @param tmpSqlSource
	 * @param dbInfo
	 * @param gridKeyAlias
	 * @return
	 * @throws SQLException
	 */
	public static SqlSourceResultVO sqlExecute(SqlStatementInfo sqlExecuteInfo, Connection conn, SqlSource tmpSqlSource, boolean gridKeyAlias) throws SQLException {
		return sqlExecute(sqlExecuteInfo, conn, tmpSqlSource, gridKeyAlias, null); 
	}
	public static SqlSourceResultVO sqlExecute(SqlStatementInfo sqlExecuteInfo, Connection conn, SqlSource tmpSqlSource, boolean gridKeyAlias, SelectExecutorHandler selectExecutorHandler) throws SQLException {
		Statement stmt = null;
		ResultSet rs  = null;
		SqlSourceResultVO ssrv = tmpSqlSource.getResult();
		
		int fetchSize = sqlExecuteInfo.getLimit(); 

		int maxRow = sqlExecuteInfo.getLimit();

		String requid = sqlExecuteInfo.getRequid$$();
		
		String executeQuery = tmpSqlSource.getQuery();
		
		SqlExecuteBean seb = SqlExecuteBean.builder().reqUid(requid).ip(sqlExecuteInfo.getIp()).build();
		
		StatementSetter statementSetter = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType())).getStatementSetter();
		
	    try{
	    	boolean hasResults;
			if(VarsqlStatementType.STATEMENT.equals(tmpSqlSource.getStatementType())){
				stmt  = conn.createStatement();
				
				JdbcUtils.setStatementFetchSize(stmt, fetchSize);
				
				// request 실행 취소 정보 추가
				SqlExecuteManager.getInstance().addStatementInfo(seb, ExecuteStatementInfo.builder().statement(stmt).sql(executeQuery).build());
				
				statementSetter.setMaxRow(stmt, maxRow, tmpSqlSource);
				hasResults = stmt.execute(executeQuery);
			}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
				CallableStatement callStatement = conn.prepareCall(executeQuery);

				// request 실행 취소 정보 추가
				SqlExecuteManager.getInstance().addStatementInfo(seb, ExecuteStatementInfo.builder().statement(callStatement).sql(executeQuery).build());
				
		        SQLParamUtils.setCallableParameter(callStatement, tmpSqlSource);
		        statementSetter.setMaxRow(callStatement, maxRow, tmpSqlSource);
		        hasResults = callStatement.execute();

		        int cursorObjIdx = -1;

		        List<GridColumnInfo> columnInfoList = new ArrayList<>();

		        boolean isOutResult = false;
		        Map resultInfo = new HashMap();

		        if(tmpSqlSource.getParamList() != null) {
		        	int idx = 1;
			        for(ParameterMapping param : tmpSqlSource.getParamList()) {

			        	if(param.getMode() == ParameterMode.OUT || param.getMode() == ParameterMode.INOUT) {
			        		isOutResult = true;
			        		SQLDataType dataType = param.getDataType();

			        		if(SQLDataType.CURSOR.equals(dataType) || SQLDataType.ORACLE_CURSOR.equals(dataType)) {
			        			cursorObjIdx= idx;
			        			hasResults = true;
			        		}else {
			        			String key = param.getProperty();
			        			key = StringUtils.isBlank(key) ? idx+"" : key;

			        			GridColumnInfo columnInfo = new GridColumnInfo();

			        			columnInfo.setKey(key);
			        			columnInfo.setLabel(key);
			        			columnInfo.setDbType(dataType != null ? dataType.name() : SQLDataType.OTHER.name() );
			        			columnInfoList.add(columnInfo);

			        			resultInfo.put(key, callStatement.getObject(idx));
			        		}
			        	}
			        	idx++;
			        }
		        }

		        if(isOutResult && !hasResults) {

		            ArrayList<Map<Object, Object>> rows = new ArrayList();
		            rows.add(resultInfo);

		            ssrv.setColumn(columnInfoList);
		            ssrv.setResultCnt(1);
		        	ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
		        	ssrv.setData(rows);
		        	return ssrv;
		        }else if(hasResults) {
		        	rs = (ResultSet)callStatement.getObject(cursorObjIdx);

		        	if(rs != null) {
		        		if(selectExecutorHandler != null) {
		        			SQLResultSetUtils.resultSetHandler(rs, sqlExecuteInfo, selectExecutorHandler, gridKeyAlias);
		        		}else {
		        			SQLResultSetUtils.resultSetHandler(rs, ssrv, sqlExecuteInfo, gridKeyAlias);
		        		}
			        	
			            ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
			            ssrv.setResultMessage(String.format("select count : %s ", new Object[] { Long.valueOf(ssrv.getResultCnt()) }));
		        	}else {
		        		ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
			            ssrv.setResultMessage("Cursor is null");
		        	}

		            return ssrv;
		        }
		        stmt = callStatement;
			}else{
				PreparedStatement pstmt = conn.prepareStatement(executeQuery);
				
				// request 실행 취소 정보 추가
				SqlExecuteManager.getInstance().addStatementInfo(seb, ExecuteStatementInfo.builder().statement(pstmt).sql(executeQuery).build());
				JdbcUtils.setStatementFetchSize(pstmt, fetchSize);
				
 				SQLParamUtils.setSqlParameter(pstmt, tmpSqlSource);
 				statementSetter.setMaxRow(pstmt, maxRow, tmpSqlSource);
				hasResults = pstmt.execute();

				stmt= pstmt;
			}
			
			if(hasResults) {
				rs = stmt.getResultSet();
				SQLResultSetUtils.resultSetHandler(rs, ssrv, sqlExecuteInfo, gridKeyAlias);
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
				ssrv.setResultMessage(String.format("select count : %s ", ssrv.getResultCnt()));
			}else{
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
				ssrv.setResultCnt(stmt.getUpdateCount());

				if(SQLCommandType.isUpdateCountCommand(tmpSqlSource.getCommand())) {
					ssrv.setResultMessage(String.format("%s count : %s", tmpSqlSource.getCommand().getCommandName(), ssrv.getResultCnt()));
				}else {
					ssrv.setResultMessage(String.format("%s success", tmpSqlSource.getCommand().getCommandName()));
				}
			}

			ssrv.setResultType(SqlDataConstants.RESULT_TYPE.SUCCESS.val());

	    }catch(SQLException e){
	    	ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
	    	ssrv.setResultType(SqlDataConstants.RESULT_TYPE.FAIL.val());
	    	ssrv.setResultMessage(String.format("error code :%s ;\nsql state : %s ;\nmessage : %s",e.getErrorCode() ,e.getSQLState() , e.getMessage()));
	    	//logger.error(" sqlData : {}", tmpSqlSource.getQuery() ,e);
	    	throw new SQLException(ssrv.getResultMessage() , e);
		} finally {
	    	JdbcUtils.close(stmt, rs);
	    }
	    
	    return ssrv;
	}
	
	/**
	 * 쿼리 실행.
	 * 
	 * @param sqlExecuteInfo
	 * @return
	 * @throws SQLException
	 */
	public static List<Map> executeQuery(SqlStatementInfo sqlExecuteInfo) throws SQLException {
		ResultSet rs  = null;
		
		String requid = StringUtils.isBlank(sqlExecuteInfo.getRequid$$()) ? VartechUtils.generateUUID() : sqlExecuteInfo.getRequid$$();
		
		String executeQuery = sqlExecuteInfo.getSql();
		
		DBVenderType dbType = DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType());
		
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(executeQuery, sqlExecuteInfo.getSqlParam(), dbType);
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection(sqlExecuteInfo.getDatabaseInfo().getVconnid());
				PreparedStatement stmt  = conn.prepareStatement(executeQuery)){
			
			// request 실행 취소 정보 추가
			SqlExecuteManager.getInstance().addStatementInfo(SqlExecuteBean.builder().reqUid(requid).ip(sqlExecuteInfo.getIp()).build(), ExecuteStatementInfo.builder().statement(stmt).sql(executeQuery).build());
			
			SQLParamUtils.setSqlParameter(stmt, sqlSource);
			rs = stmt.executeQuery();
			
			return SQLResultSetUtils.resultList(rs, dbType, false);
			
		}catch(SQLException e){
			throw e;
		} finally {
			SqlExecuteManager.getInstance().removeStatementInfo(requid);
			JdbcUtils.close(rs);
		}
	}
	
	public static List<Map> executeQuery(SqlStatementInfo sqlExecuteInfo, Connection conn) throws SQLException {
		ResultSet rs  = null;
		
		String requid = StringUtils.isBlank(sqlExecuteInfo.getRequid$$()) ? VartechUtils.generateUUID() : sqlExecuteInfo.getRequid$$();
		
		String executeQuery = sqlExecuteInfo.getSql();
		
		DBVenderType dbType = DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType());
		
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(executeQuery, sqlExecuteInfo.getSqlParam(), dbType);
		
		try(PreparedStatement stmt  = conn.prepareStatement(executeQuery)){
			
			// request 실행 취소 정보 추가
			SqlExecuteManager.getInstance().addStatementInfo(SqlExecuteBean.builder().reqUid(requid).ip(sqlExecuteInfo.getIp()).build(), ExecuteStatementInfo.builder().statement(stmt).sql(executeQuery).build());
			
			SQLParamUtils.setSqlParameter(stmt, sqlSource);
			rs = stmt.executeQuery();
			
			return SQLResultSetUtils.resultList(rs, dbType, false);
			
		}catch(SQLException e){
			throw e;
		} finally {
			SqlExecuteManager.getInstance().removeStatementInfo(requid);
			JdbcUtils.close(rs);
		}
	}
	
	/**
	 * 하나의 item 만 실행.
	 * 
	 * @param sqlExecuteInfo
	 * @return
	 * @throws SQLException
	 */
	public static Map executeQueryOneItem(SqlStatementInfo sqlExecuteInfo) throws SQLException {
		ResultSet rs  = null;
		
		String requid = StringUtils.isBlank(sqlExecuteInfo.getRequid$$()) ? VartechUtils.generateUUID() : sqlExecuteInfo.getRequid$$();
		
		String executeQuery = sqlExecuteInfo.getSql();
		
		DBVenderType dbType = DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType());
		
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(executeQuery, sqlExecuteInfo.getSqlParam(), dbType);
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection(sqlExecuteInfo.getDatabaseInfo().getVconnid());
				PreparedStatement stmt  = conn.prepareStatement(executeQuery)){
			
			// request 실행 취소 정보 추가
			SqlExecuteManager.getInstance().addStatementInfo(SqlExecuteBean.builder().reqUid(requid).ip(sqlExecuteInfo.getIp()).build(), ExecuteStatementInfo.builder().statement(stmt).sql(executeQuery).build());
			
			SQLParamUtils.setSqlParameter(stmt, sqlSource);
			rs = stmt.executeQuery();
			
			return SQLResultSetUtils.resultOne(rs, dbType, false);
			
		}catch(SQLException e){
			throw e;
		} finally {
			SqlExecuteManager.getInstance().removeStatementInfo(requid);
			JdbcUtils.close(rs);
		}
	}
	
	public static Map executeQueryOneItem(SqlStatementInfo sqlExecuteInfo, Connection conn) throws SQLException {
		ResultSet rs  = null;
		
		String requid = StringUtils.isBlank(sqlExecuteInfo.getRequid$$()) ? VartechUtils.generateUUID() : sqlExecuteInfo.getRequid$$();
		
		String executeQuery = sqlExecuteInfo.getSql();
		
		DBVenderType dbType = DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType());
		
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(executeQuery, sqlExecuteInfo.getSqlParam(), dbType);
		
		try(PreparedStatement stmt  = conn.prepareStatement(executeQuery)){
			
			// request 실행 취소 정보 추가
			SqlExecuteManager.getInstance().addStatementInfo(SqlExecuteBean.builder().reqUid(requid).ip(sqlExecuteInfo.getIp()).build(), ExecuteStatementInfo.builder().statement(stmt).sql(executeQuery).build());
			
			SQLParamUtils.setSqlParameter(stmt, sqlSource);
			rs = stmt.executeQuery();
			
			return SQLResultSetUtils.resultOne(rs, dbType, false);
		}catch(SQLException e){
			throw e;
		} finally {
			SqlExecuteManager.getInstance().removeStatementInfo(requid);
			JdbcUtils.close(rs);
		}
	}
	
	/**
	 * json string list parameter 를 map 파라미터로 변환.
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map stringParamListToMap(String param) {
		ArrayList paramList = VartechUtils.jsonStringToObject(param, ArrayList.class);
		
		Map returnParam = new HashMap();
		
		paramList.forEach(item->{
			Map paramItem = (Map)item;
			returnParam.put(paramItem.get("key"), paramItem.get("value"));
		});
		
		return returnParam; 
	}
	
	/**
	 * json string parameter map 을 hashmap으로 변환.
	 * @param param
	 * @return
	 */
	public static Map stringParamMapToMap(String param) {
		return  VartechUtils.jsonStringToObject(param, HashMap.class);
	}
	
	public static void executeBatch(Statement statement, long totalCount, int batchCount) throws SQLException {
		
		try {
			statement.executeBatch();
			statement.clearBatch();
		} catch (BatchUpdateException bue) {
			int[] updateCounts = bue.getUpdateCounts();
			
			if (updateCounts != null) {
				int failIdx = 1;
				int idx =0 ; 
				int failCount = 0;
				boolean firstFailFlag = true; 
				for (int uc : updateCounts) {
					if(uc == Statement.EXECUTE_FAILED) {
						++failCount;
						if(firstFailFlag) {
							failIdx = idx; 
							firstFailFlag = false; 
						}
						break; 
					}
					
					idx++;
				}
				throw new BatchException(bue.getMessage(), bue, totalCount - batchCount +  failIdx, failCount);
			}
			throw bue;
		} catch (SQLException se) {
			throw se;
		}
	}
	
	/**
	 * 테이블 데이터 체크 쿼리
	 * 
	 * @param target
	 * @param tableRowKey
	 * @param dataTypeFactory
	 * @return
	 */
	public static String generateSelectConditionQuery(String target, List<ColumnInfo> tableRowKey,
			DataTypeFactory dataTypeFactory) {
		StringBuilder reqQuerySb = new StringBuilder().append("select count(1) cnt from ").append(target).append(" where 1=1 ");

		if (tableRowKey.size() > 0) {
			tableRowKey.forEach(item->{
				reqQuerySb.append(" and ").append(item.getName()).append(" = ? ");
			});
		}

		return reqQuerySb.toString();
	}
	
	/**
	 * 데이터 데이터 삭제 쿼리.
	 * 
	 * @param target
	 * @param tableRowKey
	 * @param dataTypeFactory
	 * @return
	 */
	public static String generateDeleteConditionQuery(String target, List<ColumnInfo> tableRowKey,
			DataTypeFactory dataTypeFactory) {
		StringBuilder reqQuerySb = new StringBuilder().append("delete from ").append(target).append(" where 1=1 ");

		if (tableRowKey.size() > 0) {
			tableRowKey.forEach(item->{
				reqQuerySb.append(" and ").append(item.getName()).append(" = ? ");
			});
		}

		return reqQuerySb.toString();
	}
	
	
	public static void setStatementParameter(PreparedStatement statement, DataType dataType, int idx, Map rowInfo, String columnName) throws SQLException {
		try {
			dataType.getStatementHandler().setParameter(statement, idx, rowInfo.get(columnName));
		} catch (Exception e) {
			statement.setObject(idx, rowInfo.get(columnName));
		}
	}

	public static GenQueryInfo generateUpdateConditionQuery(String target, List<ExportColumnInfo> columns,
			List<ColumnInfo> tableRowKey, DataTypeFactory dataTypeFactory) {
		StringBuffer querySb = new StringBuffer();

		querySb.append("update ").append(target).append(" set ");

		StringBuilder paramSb =new StringBuilder();
		boolean firstFlag = true;
		
		List<ExportColumnInfo> newColumnList = new LinkedList<ExportColumnInfo>();
		for (ExportColumnInfo eci : columns) {
			
			if(dataTypeFactory.getDataType(eci.getTypeCode(), eci.getType()).isExcludeImportColumn()) {
				continue;
			}
			
			ColumnInfo columnInfo = tableRowKey.stream()
			        .filter(item -> item.getName().equals(eci.getName()))
			        .findFirst()
			        .orElse(null);
			
			if(columnInfo != null) continue;
			
			newColumnList.add(eci);
			
			querySb.append(firstFlag ? " ":", ").append(eci.getName()).append(" = ? ");
			
			if(firstFlag) {
				firstFlag = false;
			}
		}
		
		tableRowKey.forEach(item->{
			newColumnList.add(ExportColumnInfo.toColumnInfo(item));
		});
		
		firstFlag = true;
		for (ColumnInfo ci : tableRowKey) {
			querySb.append(firstFlag ?" where " : " and ").append(ci.getName()).append(" = ? ") ;
			
			if(firstFlag) {
				firstFlag = false;
			}
		}
		
		return new GenQueryInfo(querySb.toString(), newColumnList);
	}
}






