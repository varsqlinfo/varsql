package com.varsql.app.database.service;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResultConstants;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.database.beans.SqlLogInfo;
import com.varsql.app.database.beans.SqlParamInfo;
import com.varsql.app.database.beans.SqlUserHistoryInfo;
import com.varsql.app.database.dao.SQLDAO;
import com.varsql.app.util.SqlResultUtil;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.type.ResultType;
import com.varsql.core.common.util.DataExportUtil;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.builder.VarsqlStatementType;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.util.SQLUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.PagingUtil;
import com.vartech.common.utils.StringUtil;
import com.vartech.common.utils.StringUtil.EscapeType;
import com.vartech.common.utils.VartechUtils;

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
	private SQLDAO sqlDAO ;
	
	/**
	 * 
	 * @Method Name  : sqlFormat
	 * @Method 설명 : sql 포멧 맞추기
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 6. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseResult sqlFormat(SqlParamInfo sqlParamInfo) throws Exception {
		ResponseResult result =new ResponseResult();
		if("varsql".equals(sqlParamInfo.getCustom().get("formatType"))){
			result.setItemOne(VarsqlFormatterUtil.format(sqlParamInfo.getSql(), MetaControlFactory.getDbInstanceFactory(sqlParamInfo.getDbType()).getDbParserPrefix(), VarsqlFormatterUtil.FORMAT_TYPE.VARSQL)+"\n");
		}else{
			result.setItemOne(VarsqlFormatterUtil.format(sqlParamInfo.getSql(), MetaControlFactory.getDbInstanceFactory(sqlParamInfo.getDbType()).getDbParserPrefix())+"\n");
		}
		
		return result; 
		
	}
	
	/**
	 * 
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 데이타 보기. 
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 9. 
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param req 
	 * @return
	 * @throws Exception
	 */
	public ResponseResult sqlData(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {
		
		Map sqlParamMap = VartechUtils.stringToObject(sqlParamInfo.getSqlParam(), HashMap.class); 
		
		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(sqlParamInfo.getSql(),sqlParamMap, MetaControlFactory.getDbInstanceFactory(sqlParamInfo.getDbType()).getDbParserPrefix());
		
		List<SqlSource> sqlList = parseInfo.getItems();
		
		ArrayList<SqlSourceResultVO> reLst = new ArrayList<SqlSourceResultVO>();
		
		ResponseResult result = new ResponseResult();
		
		Connection conn = null;
		SqlSourceResultVO ssrv =null; 
		
		long stddt = System.currentTimeMillis();
		String[] mmddHH = DateUtils.dateformat("MM-dd-HH", stddt).split("-");
		
		SqlLogInfo sqlLogInfo= new SqlLogInfo();
		sqlLogInfo.setVconnid(sqlParamInfo.getVconnid());
		sqlLogInfo.setViewid(sqlParamInfo.getUserid());
		sqlLogInfo.setStartTime(stddt);
		
		sqlLogInfo.setSMm(Integer.valueOf(mmddHH[0]));
		sqlLogInfo.setSDd(Integer.valueOf(mmddHH[1]));
		sqlLogInfo.setSHh(Integer.valueOf(mmddHH[2]));
		
		sqlLogInfo.setLogSql(sqlParamInfo.getSql());
		sqlLogInfo.setUsrIp(VarsqlUtil.getClientIP(req));
		
		SqlSource tmpSqlSource =null;
		int sqldx =0,sqlSize = sqlList.size(); 
		
		String errorMsg = "";
		try {
			conn = ConnectionFactory.getInstance().getConnection(sqlParamInfo.getVconnid());
			conn.setAutoCommit(false);
			for (sqldx =0;sqldx <sqlSize; sqldx++) {
				tmpSqlSource = sqlList.get(sqldx);
				
				ssrv = new SqlSourceResultVO();
				reLst.add(ssrv);
				tmpSqlSource.setResult(ssrv);
				ssrv.setStarttime(System.currentTimeMillis());
				
				getRequestSqlData(sqlParamInfo,conn,tmpSqlSource);
				
				ssrv.setEndtime(System.currentTimeMillis());
				ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
				ssrv.setResultMessage((ssrv.getDelay())+" SECOND : "+StringUtil.escape(ssrv.getResultMessage(), EscapeType.html));
				
				sqlLogInfo.setStartTime(ssrv.getStarttime());
				sqlLogInfo.setLogSql(tmpSqlSource.getQuery());
				sqlLogInfo.setCommandType(tmpSqlSource.getCommandType());
				sqlLogInfo.setEndTime(System.currentTimeMillis());
				
				sqlLogInsert(sqlLogInfo);
			}
			
			result.setItemList(reLst);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			ssrv.setEndtime(System.currentTimeMillis());
			String tmpMsg = parseInfo.getMessage();
			tmpMsg = (tmpMsg  == null || "".equals(tmpMsg) ?"" :StringUtil.escape(parseInfo.getMessage(), EscapeType.html)+"<br/>");
						
			result.setResultCode(ResultConstants.CODE_VAL.ERROR.intVal());
			result.addCustoms("errorLine", sqldx);
			result.setMessage(tmpMsg+StringUtil.escape(ssrv.getResultMessage(), EscapeType.html));
			result.setItemOne(tmpSqlSource);
			
			errorMsg = e.getMessage();
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				SQLUtil.close(conn);
			}
		}
		
		long enddt = System.currentTimeMillis(); 
		
		SqlUserHistoryInfo  sqlUserHistoryInfo= new SqlUserHistoryInfo();
		
		sqlUserHistoryInfo.setVconnid(sqlLogInfo.getVconnid());
		sqlUserHistoryInfo.setViewid(sqlLogInfo.getViewid());
		sqlUserHistoryInfo.setHistoryId(VartechUtils.generateUUID());
		sqlUserHistoryInfo.setStartTime(VarsqlUtil.getCurrentTimestamp(stddt));
		sqlUserHistoryInfo.setEndTime(VarsqlUtil.getCurrentTimestamp(enddt));
		sqlUserHistoryInfo.setDelayTime((int) ((enddt- stddt)/1000));
		sqlUserHistoryInfo.setLogSql(sqlParamInfo.getSql());
		sqlUserHistoryInfo.setUsrIp(sqlLogInfo.getUsrIp());
		sqlUserHistoryInfo.setErrorLog(errorMsg);
		
		sqlDAO.insertUserHistory(sqlUserHistoryInfo);
		
		return  result;
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
			stmt = conn.createStatement();
			setMaxRow(stmt, maxRow);
			stmt.execute(tmpSqlSource.getQuery());
		}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
			CallableStatement callStatement = conn.prepareCall(tmpSqlSource.getQuery());
			setMaxRow(callStatement, maxRow);
			callStatement.execute();
			stmt = callStatement; 
		}else{
			PreparedStatement pstmt = conn.prepareStatement(tmpSqlSource.getQuery());
			
			List param= tmpSqlSource.getParam();
			
			if(param != null){
				for(int i =0 ;i < param.size() ;i++){
					pstmt.setObject(i+1, param.get(i));
				}
			}
			
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
	 * @param sqlParamInfo 
	 * @Method Name  : getResultData
	 * @Method 설명 : 데이타 얻기
	 * @작성일   : 2015. 4. 9. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param vconnid 
	 * @param maxRow
	 * @return
	 * @throws SQLException 
	 */
	protected void getRequestSqlData(SqlParamInfo sqlParamInfo, Connection conn, SqlSource tmpSqlSource) throws SQLException {
		Statement stmt = null;
		ResultSet rs  = null;
		SqlSourceResultVO ssrv = tmpSqlSource.getResult();
		
		int maxRow = sqlParamInfo.getLimit(VarsqlParamConstants.SQL_MAX_ROW);
		
	    try{
			stmt  = getStatement(conn, tmpSqlSource, maxRow);
			
			rs = stmt.getResultSet(); 
			
			if(rs != null){
				SqlResultUtil.resultSetHandler(rs, ssrv, sqlParamInfo, maxRow);
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
	    	ssrv.setResultMessage("error : "+e.getSQLState()+": "+ e.getLocalizedMessage());
	    	logger.error(getClass().getName()+" sqlData", e);
	    	throw new SQLException(e);
		} finally {
	    	SQLUtil.close(stmt, rs);
	    }
	}
	
	/**
	 * 
	 * @Method Name  : sqlLogInsert
	 * @Method 설명 : 사용자 sql 로그 저장
	 * @작성일   : 2015. 5. 6. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param tmpSqlSource
	 * @param ssrv
	 */
	private void sqlLogInsert(SqlLogInfo logInfo) {
		try{
		    sqlDAO.insertSqlUserLog(logInfo);
	    }catch(Exception e){
	    	logger.error(getClass().getName()+" sqlLogInsert {}", VartechUtils.reflectionToString(logInfo));
	    	logger.error(getClass().getName()+" sqlLogInsert ", e);
	    }
	}

	/**
	 * 데이타 내보내기.
	 */
	public void dataExport(SqlParamInfo sqlParamInfo, HttpServletResponse res) throws Exception {
		
		String exportType = sqlParamInfo.getExportType();
		String tmpName = sqlParamInfo.getObjectName(); 
		String reqSql = "select "+ sqlParamInfo.getColumnInfo() + " from "+tmpName;
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(reqSql);
		
		Connection conn = null;
		SqlSourceResultVO result = null;
		sqlSource.setResult(new SqlSourceResultVO());
		try {
			conn = ConnectionFactory.getInstance().getConnection(sqlParamInfo.getVconnid());
			getRequestSqlData(sqlParamInfo,conn,sqlSource);
			result = sqlSource.getResult();
		} catch (SQLException e) {
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		if("csv".equals(exportType)){
			VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(tmpName + ".csv",VarsqlConstants.CHAR_SET));
			DataExportUtil.toCSVWrite(result.getData(), result.getColumn(), res.getOutputStream());
		}else if("json".equals(exportType)){
			VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(tmpName + ".json",VarsqlConstants.CHAR_SET));
			new ObjectMapper().writeValue(res.getOutputStream(), result.getData());
		}else if("insert".equals(exportType)){
			VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(tmpName + ".sql",VarsqlConstants.CHAR_SET));
			DataExportUtil.toInsertQueryWrite(result.getData(), sqlSource.getResult().getNumberTypeFlag(), tmpName, res.getOutputStream());
		}else if("xml".equals(exportType)){
			VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(tmpName + ".xml",VarsqlConstants.CHAR_SET));
			DataExportUtil.toXmlWrite(result.getData(), result.getColumn() , res.getOutputStream());
		}else if("excel".equals(exportType)){
			VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(tmpName + ".xlsx",VarsqlConstants.CHAR_SET));
			DataExportUtil.toExcelWrite(result.getData(),result.getColumn() , res.getOutputStream());
		}
	}
	
	public void columnInfoExport(DataCommonVO paramMap,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 쿼리 저장. 
	 * @param sqlParamInfo
	 */
	public ResponseResult saveQuery(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		try{
			
			if("".equals(sqlParamInfo.getSqlId())){
				sqlParamInfo.setSqlId(VarsqlUtil.generateUUID());
			    sqlDAO.saveQueryInfo(sqlParamInfo);
			}else{
				sqlDAO.updateQueryInfo(sqlParamInfo);
			}
			
			result.setItemOne(sqlParamInfo.getSqlId());
			
	    }catch(Exception e){
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	result.setMessageCode(e.getMessage());
	    }
		return result; 
	}
	
	/**
	 * 사용자 셋팅 정보 읽기
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult userSettingInfo(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		try{
			result.setItemOne(sqlDAO.selectLastSqlInfo(sqlParamInfo));
	    }catch(Exception e){
	    	result.setResultCode(ResultConstants.CODE_VAL.ERROR.intVal());
	    	logger.error(getClass().getName()+"userSettingInfo", e);
	    	result.setMessageCode(e.getMessage());
	    }
		return result; 
	}
	/**
	 * 사용자 sql 목록 보기.
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult selectSqlList(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		
		try{
			
			int totalcnt = sqlDAO.selectSqlListTotalCnt(sqlParamInfo);
			
			int pageNo = sqlParamInfo.getCustomInfo().getInt("page", 1);
			int countPerPage = sqlParamInfo.getCustomInfo().getInt("countPerPage", 10);

			if(totalcnt > 0){
				
				int first = (pageNo - 1) * countPerPage + 1;
				int last = first + countPerPage - 1;
				sqlParamInfo.addCustomInfo("first", Integer.valueOf(first));
				sqlParamInfo.addCustomInfo("last", Integer.valueOf(last));
				sqlParamInfo.addCustomInfo("rows", countPerPage);
				result.setItemList(sqlDAO.selectSqlList(sqlParamInfo));
			}else{
				result.setItemList(null);
			}
			result.setPage(PagingUtil.getPageObject(totalcnt, pageNo,countPerPage));
	    }catch(Exception e){
	    	result.setResultCode(ResultConstants.CODE_VAL.ERROR.intVal());
	    	logger.error(getClass().getName()+"saveQuery", e);
	    	result.setMessageCode(e.getMessage());
	    }
		return result; 
	}
	
	/**
	 * sql 저장 정보 삭제 .
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult deleteSqlSaveInfo(SqlParamInfo sqlParamInfo) {

		ResponseResult result = new ResponseResult();
		try{
			result.setItemOne( sqlDAO.deleteSqlSaveInfo(sqlParamInfo));
			
	    }catch(Exception e){
	    	result.setResultCode(ResultConstants.CODE_VAL.ERROR.intVal());
	    	logger.error(getClass().getName()+"deleteSqlSaveInfo", e);
	    	result.setMessageCode(e.getMessage());
	    }
		return result; 
	}
	
}