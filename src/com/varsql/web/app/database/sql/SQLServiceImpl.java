package com.varsql.web.app.database.sql;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varsql.common.type.ResultType;
import com.varsql.common.util.DataExportUtil;
import com.varsql.db.ConnectionFactory;
import com.varsql.sql.builder.SqlSource;
import com.varsql.sql.builder.SqlSourceBuilder;
import com.varsql.sql.builder.SqlSourceResultVO;
import com.varsql.sql.builder.VarsqlStatementType;
import com.varsql.sql.format.VarsqlFormatterDb2;
import com.varsql.sql.util.SQLUtil;
import com.varsql.web.app.database.bean.SqlParamInfo;
import com.varsql.web.common.constants.ResultConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.SqlResultUtil;
import com.varsql.web.util.VarsqlUtil;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.PagingUtil;
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
	 * @param sqlParamInfo
	 * @return
	 * @throws Exception
	 */
	public String sqlFormat(SqlParamInfo sqlParamInfo) throws Exception {
		StringBuffer sqlFormatSb = new StringBuffer();
		
//		List<SqlSource> sqlList=new SqlSourceBuilder().parse(paramMap.getString(VarsqlParamConstants.SQL));
//		for (SqlSource tmpSqlSource : sqlList) {
//			
//			sqlFormatSb.append(new VarsqlFormatterDb2().execute(tmpSqlSource.getQuery())).append("\n");
//		}
		sqlFormatSb.append(new VarsqlFormatterDb2().execute(sqlParamInfo.getSql())).append("\n");
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
	 * @param sqlParamInfo
	 * @return
	 * @throws Exception
	 */
	public ArrayList sqlData(SqlParamInfo sqlParamInfo) throws Exception {
		String reqSql = sqlParamInfo.getSql();
		
		Map sqlParamMap = VartechUtils.stringToObject(sqlParamInfo.getSqlParam(), HashMap.class); 
		
		List<SqlSource> sqlList=new SqlSourceBuilder().parse(reqSql,sqlParamMap);
		
		
		String vconnid = sqlParamInfo.getVconnid();
		
		ArrayList reLst = new ArrayList();
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			
			for (SqlSource tmpSqlSource : sqlList) {
				
				getRequestSqlData(sqlParamInfo,conn,tmpSqlSource, vconnid);
				
				reLst.add(tmpSqlSource.getResult());
			}
			
			return reLst;
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		return  new ArrayList();
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
	 */
	protected Object getRequestSqlData(SqlParamInfo sqlParamInfo, Connection conn, SqlSource tmpSqlSource, String vconnid) {
		Statement stmt = null;
		ResultSet rs  = null;
		Object reVal=null;
		SqlSourceResultVO ssrv = new SqlSourceResultVO();
		
		int maxRow = sqlParamInfo.getLimit(VarsqlParamConstants.SQL_MAX_ROW);
		
		ssrv.setStarttime(System.currentTimeMillis());
		
	    try{
			stmt  = getStatement(conn, tmpSqlSource, maxRow);
			
			rs = stmt.getResultSet(); 
			
			if(rs != null){
				SqlResultUtil.resultSetHandler(rs, ssrv, sqlParamInfo, maxRow,vconnid);
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
	    	//logger.error(getClass().getName()+" sqlData", e);
		} finally {
	    	SQLUtil.close(stmt, rs);
	    }
	    
	    ssrv.setEndtime(System.currentTimeMillis());
		ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
		ssrv.setResultMessage((ssrv.getDelay())/1000.0 +" SECOND : "+ssrv.getResultMessage());
		
	    tmpSqlSource.setResult(ssrv);
	    
	    sqlLogInsert(sqlParamInfo,tmpSqlSource , ssrv);
	    return reVal;
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
	private void sqlLogInsert(SqlParamInfo sqlParamInfo, SqlSource tmpSqlSource, SqlSourceResultVO ssrv) {
		try{
	    	DataCommonVO logInfoMap = new DataCommonVO();
	    	
	    	java.sql.Timestamp stime = new java.sql.Timestamp(ssrv.getStarttime());
	    	calendar.setTime(stime);
	    	
		    logInfoMap.put("vconnid", sqlParamInfo.getVconnid());
		    logInfoMap.put("uid", sqlParamInfo.getUserid());
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
	public void dataExport(SqlParamInfo sqlParamInfo, HttpServletResponse res) throws Exception {
		
		String exportType = sqlParamInfo.getExportType();
		String tmpName = sqlParamInfo.getObjectName(); 
		String reqSql = "select "+ sqlParamInfo.getColumnInfo() + " from "+tmpName;
		SqlSource sqlSource = new SqlSourceBuilder().getSqlSource(reqSql);
		
		String vconnid = sqlParamInfo.getVconnid();
		
		Connection conn = null;
		SqlSourceResultVO result = new SqlSourceResultVO();
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			getRequestSqlData(sqlParamInfo,conn,sqlSource, vconnid);
			result = sqlSource.getResult();
		} catch (SQLException e) {
			logger.error(getClass().getName()+"sqlData", e);
		}finally{
			SQLUtil.close(conn);
		}
		
		res.setContentType("application/octet-stream;charset=UTF-8");
		res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ";");
		res.setHeader("Content-Transfer-Encoding", "binary;");
		res.setCharacterEncoding("utf-8");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1;");
		
		if("csv".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".csv;");
			DataExportUtil.toCSVWrite(result.getData(), result.getColumn(), res.getOutputStream());
		}else if("json".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".json;");
			new ObjectMapper().writeValue(res.getOutputStream(), result.getData());
		}else if("insert".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".sql;");
			DataExportUtil.toInsertQueryWrite(result.getData(), sqlSource.getResult().getNumberTypeFlag(), tmpName, res.getOutputStream());
		}else if("xml".equals(exportType)){
			res.setHeader("Content-Disposition", "attachment; filename=" + tmpName + ".xml;");
			DataExportUtil.toXmlWrite(result.getData(), result.getColumn() , res.getOutputStream());
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