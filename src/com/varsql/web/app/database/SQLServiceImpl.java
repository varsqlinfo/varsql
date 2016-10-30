package com.varsql.web.app.database;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.common.type.ResultType;
import com.varsql.common.util.DataExportUtil;
import com.varsql.db.ConnectionFactory;
import com.varsql.format.VarsqlFormatterDb2;
import com.varsql.sql.SQLUtil;
import com.varsql.sql.builder.SqlSource;
import com.varsql.sql.builder.SqlSourceBuilder;
import com.varsql.sql.builder.SqlSourceResultVO;
import com.varsql.sql.builder.VarsqlStatementType;
import com.varsql.web.common.constants.ResultConstants;
import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.PagingUtil;
import com.varsql.web.util.SqlResultUtil;
import com.varsql.web.util.VarsqlUtil;

/**
 * 
 * @FileName  : SQLServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class SQLServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(SQLServiceImpl.class);
	
	@Autowired
	SQLDAO sqlDAO ;
	
	private Calendar calendar = Calendar.getInstance();
	
	public String selectQna(DataCommonVO paramMap) {
		return "";
	}
	
	/**
	 * 
	 * @Method Name  : sqlFormat
	 * @Method 설명 : sql 포멧 맞추기
	 * @Method override : @see com.varsql.web.app.database.SQLService#sqlFormat(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 6. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String sqlFormat(DataCommonVO paramMap) throws Exception {
		StringBuffer sqlFormatSb = new StringBuffer();
		
//		List<SqlSource> sqlList=new SqlSourceBuilder().parse(paramMap.getString(VarsqlParamConstants.SQL));
//		for (SqlSource tmpSqlSource : sqlList) {
//			
//			sqlFormatSb.append(new VarsqlFormatterDb2().execute(tmpSqlSource.getQuery())).append("\n");
//		}
		sqlFormatSb.append(new VarsqlFormatterDb2().execute(paramMap.getString(VarsqlParamConstants.SQL))).append("\n");
		return sqlFormatSb.toString();
	}
	
	/**
	 * 
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 데이타 보기. 
	 * @Method override : @see com.varsql.web.app.database.SQLService#sqlData(com.varsql.web.common.vo.DataCommonVO)
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 9. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String sqlData(DataCommonVO paramMap) throws Exception {
		String reqSql = paramMap.getString(VarsqlParamConstants.SQL);
		List<SqlSource> sqlList=new SqlSourceBuilder().parse(reqSql);
		
		
		String vconnid = paramMap.getString(VarsqlParamConstants.VCONNID);
		
		ArrayList reLst = new ArrayList();
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			
			for (SqlSource tmpSqlSource : sqlList) {
				
				getRequestSqlData(paramMap,conn,tmpSqlSource);
				
				reLst.add(tmpSqlSource.getResult());
			}
			
			return VarsqlUtil.objectToString(reLst);
		} catch (SQLException e) {
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		return "";
	}
	
	/**
	 * 
	 * @Method Name  : getStatement
	 * @Method 설명 : 실행할 statement 얻기
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param maxRow
	 * @return
	 * @throws SQLException
	 */
	private Statement getStatement(Connection conn, SqlSource tmpSqlSource, int maxRow) throws SQLException {
		
		Statement stmt = null; 
		if(VarsqlStatementType.STATEMENT.equals(tmpSqlSource.getStatementType())){
			stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
			setMaxRow(stmt, maxRow);
			stmt.execute(tmpSqlSource.getQuery());
		}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
			CallableStatement callStatement = conn.prepareCall(tmpSqlSource.getQuery());
			setMaxRow(callStatement, maxRow);
			callStatement.execute();
			stmt = callStatement; 
		}else{
			PreparedStatement pstmt = conn.prepareStatement(tmpSqlSource.getQuery(), ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
			setMaxRow(pstmt, maxRow);
			pstmt.execute();
			stmt = pstmt; 
		}
		return stmt;
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
	 * @throws SQLException
	 */
	private void setMaxRow(Statement stmt, int maxRow) throws SQLException {
		//stmt.setMaxRows(maxRow);
	}
	
	/**
	 * 
	 * @param paramMap 
	 * @Method Name  : getResultData
	 * @Method 설명 : 데이타 얻기
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param maxRow
	 * @return
	 */
	protected Object getRequestSqlData(DataCommonVO paramMap, Connection conn, SqlSource tmpSqlSource) {
		Statement stmt = null;
		ResultSet rs  = null;
		Object reVal=null;
		SqlSourceResultVO ssrv = new SqlSourceResultVO();
		
		int maxRow = paramMap.getInt(VarsqlParamConstants.LIMIT, VarsqlParamConstants.SQL_MAX_ROW);
		
		ssrv.setStarttime(System.currentTimeMillis());
		
	    try{
			stmt  = getStatement(conn, tmpSqlSource, maxRow);
			
			rs = stmt.getResultSet(); 
			
			if(rs != null){
				SqlResultUtil.resultSetHandler(rs, ssrv, paramMap, maxRow);
				ssrv.setViewType("grid");
				ssrv.setResultMessage("success result count : "+ssrv.getResultCnt());
			}else{
				ssrv.setViewType("msg");
				ssrv.setResultCnt(stmt.getUpdateCount());
				ssrv.setResultMessage("success update count : "+ ssrv.getResultCnt());
			}
			    
			ssrv.setResultType(ResultType.SUCCESS.name());
			
	    }catch(SQLException e){
	    	ssrv.setViewType("msg");
	    	ssrv.setResultType(ResultType.FAIL.name());
	    	//e.fillInStackTrace()
	    	ssrv.setResultMessage("error : "+e.getSQLState()+": "+ e.getLocalizedMessage());
	    	logger.error(getClass().getName()+" sqlData", e);
		} finally {
	    	SQLUtil.close(stmt, rs);
	    }
	    
	    ssrv.setEndtime(System.currentTimeMillis());
		ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
		ssrv.setResultMessage((ssrv.getDelay())/1000.0 +" SECOND : "+ssrv.getResultMessage());
		
	    tmpSqlSource.setResult(ssrv);
	    
	    sqlLogInsert(paramMap,tmpSqlSource , ssrv);
	    return reVal;
	}
	
	/**
	 * 
	 * @Method Name  : sqlLogInsert
	 * @Method 설명 : 사용자 sql 로그 저장
	 * @작성일   : 2015. 5. 6. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @param tmpSqlSource
	 * @param ssrv
	 */
	private void sqlLogInsert(DataCommonVO paramMap, SqlSource tmpSqlSource, SqlSourceResultVO ssrv) {
		try{
	    	DataCommonVO logInfoMap = new DataCommonVO();
	    	
	    	java.sql.Timestamp stime = new java.sql.Timestamp(ssrv.getStarttime());
	    	calendar.setTime(stime);
	    	
		    logInfoMap.put("vconnid", paramMap.getString(VarsqlParamConstants.VCONNID));
		    logInfoMap.put("uid", paramMap.getString(UserConstants.UID));
		    logInfoMap.put("start_time", stime);
		    logInfoMap.put("s_mm", calendar.get(Calendar.MONTH)+1);
		    logInfoMap.put("s_dd", calendar.get(Calendar.DATE));
		    logInfoMap.put("s_hh", calendar.get(Calendar.HOUR_OF_DAY));
		    logInfoMap.put("end_time", new java.sql.Timestamp(ssrv.getEndtime()));
		    logInfoMap.put("delay_time", ssrv.getDelay());
		    logInfoMap.put("log_sql", tmpSqlSource.getQuery());
		    logInfoMap.put("result_count", ssrv.getResultCnt());
		    logInfoMap.put("command_type", tmpSqlSource.getCommandType());
		    
		    sqlDAO.insertSqlUserLog(logInfoMap);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}

	/**
	 * 데이타 내보내기.
	 */
	public void dataExport(DataCommonVO paramMap, HttpServletResponse res) throws Exception {
		
		String exportType = paramMap.getString(VarsqlParamConstants.EXPORT_TYPE);
		String tmpName = paramMap.getString(VarsqlParamConstants.DB_OBJECT_NAME); 
		String reqSql = "select "+ paramMap.getString(VarsqlParamConstants.EXPORT_COLUMN_INFO) + " from "+tmpName;
		SqlSource sqlSource = new SqlSourceBuilder().getSqlSource(reqSql);
		
		String vconnid = paramMap.getString(VarsqlParamConstants.VCONNID);
		
		Connection conn = null;
		List result = new ArrayList();
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			getRequestSqlData(paramMap,conn,sqlSource);
			result = sqlSource.getResult().getData();
		} catch (SQLException e) {
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ";");
		res.setHeader("Content-Transfer-Encoding", "binary;");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1;");
		
		if("csv".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".csv;");
			DataExportUtil.toCSVWrite(result, '\t', res.getOutputStream());
		}else if("json".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".json;");
			new ObjectMapper().writeValue(res.getOutputStream(), result);
		}else if("insert".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".sql;");
			DataExportUtil.toInsertQueryWrite(result, sqlSource.getResult().getNumberTypeFlag(), tmpName, res.getOutputStream());
		}else if("xml".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".xml;");
			DataExportUtil.toXmlWrite(result, "row", res.getOutputStream());
		}
	}
	
	public void columnInfoExport(DataCommonVO paramMap,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 쿼리 저장. 
	 * @param paramMap
	 */
	public Map saveQuery(DataCommonVO paramMap) {
		Map reval =  new HashMap();
		try{
			
			if("".equals(paramMap.getString("sql_id"))){
				paramMap.put("sql_id",VarsqlUtil.generateUUID());
			    sqlDAO.saveQueryInfo(paramMap);
			    reval.put("sql_id", paramMap.get("sql_id"));
			}else{
				sqlDAO.updateQueryInfo(paramMap);
			}
		    
		    reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	/**
	 * 사용자 셋팅 정보 읽기
	 * @param paramMap
	 * @return
	 */
	public Map userSettingInfo(DataCommonVO paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put("sqlInfo", sqlDAO.selectLastSqlInfo(paramMap));
		    reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	/**
	 * 사용자 sql 목록 보기.
	 * @param paramMap
	 * @return
	 */
	public Map selectSqlList(DataCommonVO paramMap) {
		Map reval =  new HashMap();
		try{
			
			int totalcnt = sqlDAO.selectSqlListTotalCnt(paramMap);
			
			int pageNo = paramMap.getInt("page", 1);
			int countPerPage = paramMap.getInt("countPerPage", 10);

			if(totalcnt > 0){
				
				int first = (pageNo - 1) * countPerPage + 1;
				int last = first + countPerPage - 1;
				paramMap.put("first", Integer.valueOf(first));
				paramMap.put("last", Integer.valueOf(last));
				paramMap.put("rows", countPerPage);
				
				reval.put(ResultConstants.PAGING, PagingUtil.getPageObject(totalcnt, pageNo,countPerPage));
				reval.put(ResultConstants.RESULT_ITEMS, sqlDAO.selectSqlList(paramMap));
			}else{
				reval.put(ResultConstants.PAGING, PagingUtil.getPageObject(totalcnt, pageNo,countPerPage));
				reval.put(ResultConstants.RESULT_ITEMS, new ArrayList());
			}
			
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	/**
	 * sql 저장 정보 삭제 .
	 * @param paramMap
	 * @return
	 */
	public Map deleteSqlSaveInfo(DataCommonVO paramMap) {

		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT, sqlDAO.deleteSqlSaveInfo(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	
	/**
	 * sql 보내기
	 * @param paramMap
	 * @return
	 */
	public Map insertSendSqlInfo(DataCommonVO paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT, sqlDAO.insertSendSqlInfo(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	/**
	 * 사용자 검색.
	 * @param paramMap
	 * @return
	 */
	public Map selectSearchUserList(DataCommonVO paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT_ITEMS, sqlDAO.selectSearchUserList(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
}