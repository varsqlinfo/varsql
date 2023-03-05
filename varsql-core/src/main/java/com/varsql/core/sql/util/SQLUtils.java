package com.varsql.core.sql.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.SqlExecuteManager;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.beans.SqlExecuteDTO;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.builder.VarsqlStatementType;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMode;
import com.varsql.core.sql.type.SQLCommandType;
import com.varsql.core.sql.type.SQLDataType;
import com.vartech.common.utils.StringUtils;

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
	public static SqlSourceResultVO getSqlExecute(SqlExecuteDTO sqlExecuteInfo, Connection conn, SqlSource tmpSqlSource, boolean gridKeyAlias) throws SQLException {
		return getSqlExecute(sqlExecuteInfo, conn, tmpSqlSource, gridKeyAlias, null); 
	}
	public static SqlSourceResultVO getSqlExecute(SqlExecuteDTO sqlExecuteInfo, Connection conn, SqlSource tmpSqlSource, boolean gridKeyAlias, SelectExecutorHandler selectExecutorHandler) throws SQLException {
		Statement stmt = null;
		ResultSet rs  = null;
		SqlSourceResultVO ssrv = tmpSqlSource.getResult();

		int maxRow = sqlExecuteInfo.getLimit();

		String requid = sqlExecuteInfo.get_requid_();
		
		String executeQuery = tmpSqlSource.getQuery(); 
	    try{
	    	boolean hasResults;
			if(VarsqlStatementType.STATEMENT.equals(tmpSqlSource.getStatementType())){
				stmt  = conn.createStatement();
				
				JdbcUtils.setStatementFetchSize(stmt, sqlExecuteInfo.getLimit());
				
				SqlExecuteManager.getInstance().setStatementInfo(requid, stmt);
				
				setMaxRow(stmt, maxRow, tmpSqlSource);
				hasResults = stmt.execute(executeQuery);
			}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
				CallableStatement callStatement = conn.prepareCall(executeQuery);

				SqlExecuteManager.getInstance().setStatementInfo(requid, callStatement);
				
		        SQLParamUtils.setCallableParameter(callStatement, tmpSqlSource);
		        setMaxRow(callStatement, maxRow, tmpSqlSource);
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
				
				SqlExecuteManager.getInstance().setStatementInfo(requid, pstmt);
				JdbcUtils.setStatementFetchSize(pstmt, sqlExecuteInfo.getLimit());
				
 				SQLParamUtils.setSqlParameter(pstmt, tmpSqlSource);
				setMaxRow(pstmt, maxRow, tmpSqlSource);
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
	 *
	 * @Method Name  : setMaxRow
	 * @Method 설명 : row 갯수 셋팅
	 * @작성일   : 2015. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param stmt
	 * @param maxRow
	 * @param tmpSqlSource 
	 * @throws SQLException
	 */
	public static void setMaxRow(Statement stmt, int maxRow, SqlSource tmpSqlSource) throws SQLException {
		if(tmpSqlSource.getCommand().isSelectCommand()) {
			stmt.setMaxRows(maxRow);
		}else if(SQLCommandType.OTHER.equals( tmpSqlSource.getCommand())) {
			stmt.setMaxRows(maxRow);
		}
	}
}
