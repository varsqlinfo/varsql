package com.varsql.web.app.database.service;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.DataExportUtil;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.builder.VarsqlCommandType;
import com.varsql.core.sql.builder.VarsqlStatementType;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.util.SqlParamUtils;
import com.varsql.core.sql.util.SqlUtils;
import com.varsql.web.constants.SqlDataConstants;
import com.varsql.web.dto.sql.SqlExecuteDTO;
import com.varsql.web.dto.sql.SqlGridDownloadInfo;
import com.varsql.web.dto.sql.SqlLogInfoDTO;
import com.varsql.web.exception.VarsqlResultConvertException;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.repository.sql.SqlHistoryEntityRepository;
import com.varsql.web.repository.sql.SqlStatisticsEntityRepository;
import com.varsql.web.util.SqlResultUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.exception.VartechRuntimeException;
import com.vartech.common.utils.DateUtils;
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
	private final Logger logger = LoggerFactory.getLogger(SQLServiceImpl.class);

	@Autowired
	private SqlHistoryEntityRepository sqlHistoryEntityRepository;

	@Autowired
	private SqlStatisticsEntityRepository sqlStatisticsEntityRepository;

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
	public ResponseResult sqlFormat(SqlExecuteDTO sqlParamInfo) throws Exception {
		ResponseResult result =new ResponseResult();

		String dbParserPrefix = DBType.getDbParser(sqlParamInfo.getDbType());

		if("varsql".equals(sqlParamInfo.getCustom().get("formatType"))){
			result.setItemOne(VarsqlFormatterUtil.format(sqlParamInfo.getSql(),dbParserPrefix , VarsqlFormatterUtil.FORMAT_TYPE.VARSQL)+"\n");
		}else{
			result.setItemOne(VarsqlFormatterUtil.format(sqlParamInfo.getSql(),dbParserPrefix )+"\n");
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
	 * @param sqlExecuteInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public ResponseResult sqlData(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req) throws Exception {

		Map sqlParamMap = VartechUtils.jsonStringToObject(sqlExecuteInfo.getSqlParam(), HashMap.class);

		DatabaseInfo dbinfo = SecurityUtil.userDBInfo(sqlExecuteInfo.getConuid());

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(sqlExecuteInfo.getSql(),sqlParamMap, DBType.getDbParser(sqlExecuteInfo.getDbType()));

		List<SqlSource> sqlList = parseInfo.getItems();

		int limit = sqlExecuteInfo.getLimit();

		if(!SecurityUtil.isAdmin()) {
			sqlExecuteInfo.setLimit(limit > dbinfo.getMaxSelectCount() ? dbinfo.getMaxSelectCount() : limit);
		}

		ArrayList<SqlSourceResultVO> reLst = new ArrayList<SqlSourceResultVO>();

		ResponseResult result = new ResponseResult();

		Connection conn = null;
		SqlSourceResultVO ssrv =null;

		long stddt = System.currentTimeMillis();
		String[] mmddHH = DateUtils.dateformat("MM-dd-HH", stddt).split("-");

		SqlLogInfoDTO sqlLogInfo= new SqlLogInfoDTO();
		sqlLogInfo.setVconnid(sqlExecuteInfo.getVconnid());
		sqlLogInfo.setViewid(sqlExecuteInfo.getViewid());
		sqlLogInfo.setStartTime(stddt);

		sqlLogInfo.setSMm(Integer.valueOf(mmddHH[0]));
		sqlLogInfo.setSDd(Integer.valueOf(mmddHH[1]));
		sqlLogInfo.setSHh(Integer.valueOf(mmddHH[2]));

		sqlLogInfo.setUsrIp(VarsqlUtils.getClientIp(req));

		SqlSource tmpSqlSource =null;
		int sqldx =0,sqlSize = sqlList.size();

		String errorMsg = "";
		try {
			conn = ConnectionFactory.getInstance().getConnection(sqlExecuteInfo.getVconnid());
			conn.setAutoCommit(false);
			for (sqldx =0;sqldx <sqlSize; sqldx++) {
				tmpSqlSource = sqlList.get(sqldx);

				ssrv = new SqlSourceResultVO();
				reLst.add(ssrv);
				tmpSqlSource.setResult(ssrv);
				ssrv.setStarttime(System.currentTimeMillis());

				getRequestSqlData(sqlExecuteInfo, conn, tmpSqlSource, dbinfo, true);

				ssrv.setEndtime(System.currentTimeMillis());
				ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
				ssrv.setResultMessage((ssrv.getDelay())+" SECOND : "+StringUtil.escape(ssrv.getResultMessage(), EscapeType.html));

				sqlLogInfo.setStartTime(ssrv.getStarttime());
				sqlLogInfo.setCommandType(tmpSqlSource.getCommandType());
				sqlLogInfo.setEndTime(ssrv.getEndtime());

				sqlLogInsert(sqlLogInfo);

				if(SqlDataConstants.VIEWTYPE.GRID.val().equals(ssrv.getViewType())) {
					break;
				}
			}

			result.setItemList(reLst);
			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();

			errorMsg = e.getMessage();

			if(e instanceof VarsqlResultConvertException){
				result.setResultCode(VarsqlAppCode.EC_SQL_RESULT_CONVERT.code());
				ssrv= ((VarsqlResultConvertException)e).getSsrv();
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
			}else {
				boolean ssrvNullFlag = false;
				if(ssrv==null) {
					ssrvNullFlag = true;
					ssrv = new SqlSourceResultVO();
				}

				ssrv.setEndtime(System.currentTimeMillis());
				String tmpMsg = parseInfo.getMessage();
				tmpMsg = (tmpMsg  == null || "".equals(tmpMsg) ?"" :StringUtil.escape(parseInfo.getMessage(), EscapeType.html)+"<br/>");

				if(e instanceof ConnectionFactoryException) {
					if(((ConnectionFactoryException)e).getErrorCode() ==VarsqlAppCode.EC_DB_POOL_CLOSE.code()) {
						result.setResultCode(VarsqlAppCode.EC_DB_POOL_CLOSE.code());
					}else {
						result.setResultCode(VarsqlAppCode.EC_DB_POOL_ERROR.code());
					}
				}else {
					result.setResultCode(VarsqlAppCode.EC_SQL.code());
				}

				result.setMessage(tmpMsg+StringUtil.escape(ssrv.getResultMessage(), EscapeType.html));

				if(ssrvNullFlag) {
					result.setMessage(errorMsg);
				}
			}

			result.addCustoms("errorLine", sqldx);
			result.setItemOne(tmpSqlSource);

			if(VarsqlUtils.isRuntimelocal()) {
				logger.error(getClass().getName()+" sqlData : ", e);
			}
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				SqlUtils.close(conn);
			}
		}

		long enddt = System.currentTimeMillis();

		try {
			sqlHistoryEntityRepository.save(SqlHistoryEntity.builder()
				.vconnid(sqlLogInfo.getVconnid())
				.viewid(sqlLogInfo.getViewid())
				.startTime(VarsqlUtils.getTimestamp(stddt))
				.endTime(VarsqlUtils.getTimestamp(enddt))
				.delayTime((int) ((enddt- stddt)/1000))
				.logSql(sqlExecuteInfo.getSql())
				.usrIp(sqlLogInfo.getUsrIp())
				.errorLog(errorMsg)
				.build()
			);
		}catch(Throwable e) {
			logger.error(" sqlData sqlHistoryEntity : ", e);
		}

		return  result;
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
		stmt.setMaxRows(maxRow);
	}

	/**
	 *
	 * @param sqlExecuteInfo
	 * @Method Name  : getResultData
	 * @Method 설명 : 데이타 얻기
	 * @작성일   : 2015. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param dbinfo
	 * @param vconnid
	 * @param maxRow
	 * @return
	 * @throws SQLException
	 */
	protected void getRequestSqlData(SqlExecuteDTO sqlExecuteInfo, Connection conn, SqlSource tmpSqlSource, DatabaseInfo dbInfo ,boolean gridKeyAlias) throws SQLException {
		Statement stmt = null;
		ResultSet rs  = null;
		SqlSourceResultVO ssrv = tmpSqlSource.getResult();

		int maxRow = sqlExecuteInfo.getLimit();

	    try{
	    	boolean hasResults;
			if(VarsqlStatementType.STATEMENT.equals(tmpSqlSource.getStatementType())){
				stmt  = conn.createStatement();
				setMaxRow(stmt, maxRow);
				hasResults = stmt.execute(tmpSqlSource.getQuery());
			}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
				CallableStatement callStatement = conn.prepareCall(tmpSqlSource.getQuery());
				//setMaxRow(callStatement, maxRow);
				hasResults = callStatement.execute();
				stmt= callStatement;
			}else{
				PreparedStatement pstmt = conn.prepareStatement(tmpSqlSource.getQuery());
				setSqlParameter(pstmt , maxRow, tmpSqlSource);
				setMaxRow(pstmt, maxRow);
				hasResults = pstmt.execute();

				stmt= pstmt;
			}

			if(hasResults==true) {
				rs = stmt.getResultSet();
				SqlResultUtils.resultSetHandler(rs, ssrv, sqlExecuteInfo, maxRow , gridKeyAlias);
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
				ssrv.setResultMessage(String.format("select count : %s ",ssrv.getResultCnt()));
			}else{
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
				ssrv.setResultCnt(stmt.getUpdateCount());

				if(VarsqlCommandType.isUpdateCountCommand(tmpSqlSource.getCommandType())) {
					ssrv.setResultMessage(String.format("%s count : %s" , tmpSqlSource.getCommandType(), ssrv.getResultCnt()));
				}else {
					ssrv.setResultMessage(String.format("%s success" , tmpSqlSource.getCommandType()));
				}

			}

			ssrv.setResultType(SqlDataConstants.RESULT_TYPE.SUCCESS.val());

	    }catch(SQLException e){
	    	ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
	    	ssrv.setResultType(SqlDataConstants.RESULT_TYPE.FAIL.val());
	    	ssrv.setResultMessage(String.format("error code :%s ;\nsql state : %s ;\nmessage : %s",e.getErrorCode() ,e.getSQLState() , e.getMessage()));
	    	logger.error(" sqlData : {}", tmpSqlSource.getQuery() ,e);
	    	throw new SQLException(ssrv.getResultMessage() , e);
		} finally {
	    	SqlUtils.close(stmt, rs);
	    }
	}

	private void setSqlParameter(PreparedStatement pstmt, int maxRow, SqlSource tmpSqlSource) throws SQLException {
		List<ParameterMapping> paramList= tmpSqlSource.getParamList();

		if(paramList != null){
			Map orginParamMap = tmpSqlSource.getOrginSqlParam();

			for(int i =1 ;i <= paramList.size() ;i++){
				ParameterMapping param = paramList.get(i-1);
				Object objVal;
				if(param.isFunction()) {
					objVal = SqlParamUtils.functionValue(param, orginParamMap);
				}else {
					objVal = orginParamMap.get(param.getProperty());
				}

				if(param.getJdbcType()==null) {
					pstmt.setObject(i, objVal);
				}else {
					param.getJdbcType().setParameter(pstmt, i, objVal);
				}
			}
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
	private void sqlLogInsert(SqlLogInfoDTO logInfo) {
		try{
			sqlStatisticsEntityRepository.save(SqlStatisticsEntity.builder()
				.vconnid(logInfo.getVconnid())
				.viewid(logInfo.getViewid())
				.startTime(VarsqlUtils.getLocalDateTime(logInfo.getStartTime()))
				.endTime(VarsqlUtils.getLocalDateTime(logInfo.getEndTime()))
				.delayTime(logInfo.getDelayTime())
				.sMm(logInfo.getSMm())
				.sDd(logInfo.getSDd())
				.sHh(logInfo.getSHh())
				.resultCount(logInfo.getResultCount())
				.commandType(logInfo.getCommandType())
				.build()
			);
	    }catch(Exception e){
	    	logger.error(" sqlLogInsert {}", e.getMessage() , e);
	    }
	}

	/**
	 * 데이타 내보내기.
	 * @param paramMap
	 */
	public void dataExport(ParamMap paramMap, SqlExecuteDTO sqlParamInfo, HttpServletResponse res) throws Exception {

		String exportType = sqlParamInfo.getExportType();

		String tmpName = sqlParamInfo.getObjectName();

		if(!sqlParamInfo.getBaseSchema().equals(sqlParamInfo.getSchema())) {
			tmpName = sqlParamInfo.getSchema()+"."+tmpName;
		}

		String reqSql = "select "+ sqlParamInfo.getColumnInfo() + " from "+tmpName;
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(reqSql);

		Connection conn = null;
		SqlSourceResultVO result = null;
		sqlSource.setResult(new SqlSourceResultVO());

		DatabaseInfo dbinfo = SecurityUtil.userDBInfo(sqlParamInfo.getConuid());

		try {
			conn = ConnectionFactory.getInstance().getConnection(sqlParamInfo.getVconnid());
			getRequestSqlData(sqlParamInfo,conn,sqlSource, dbinfo, false);
			result = sqlSource.getResult();
		} catch (SQLException e) {
			logger.error(getClass().getName()+" dataExport ", e);
		}finally{
			SqlUtils.close(conn);
		}

		if(result == null) {
			throw new VartechRuntimeException("sql data empty");
		}

		String exportFileName = paramMap.getString("fileName", tmpName);

		OutputStream os = res.getOutputStream();

		try {
			if("csv".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".csv",VarsqlConstants.CHAR_SET));
				DataExportUtil.toCSVWrite(result.getData(), result.getColumn(), os);
			}else if("json".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".json",VarsqlConstants.CHAR_SET));
				new ObjectMapper().writeValue(os, result.getData());
			}else if("insert".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".sql",VarsqlConstants.CHAR_SET));
				DataExportUtil.toInsertQueryWrite(result.getData(), sqlSource.getResult().getNumberTypeFlag(), tmpName, os);
			}else if("xml".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".xml",VarsqlConstants.CHAR_SET));
				DataExportUtil.toXmlWrite(result.getData(), result.getColumn() , os);
			}else if("excel".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".xlsx",VarsqlConstants.CHAR_SET));
				DataExportUtil.toExcelWrite(result.getData(),result.getColumn() , os);
			}

			if(os !=null) os.close();
		}catch(Exception e) {
			logger.error(getClass().getName()+" dataExport {}", e.getMessage());
	    	logger.error(getClass().getName()+" dataExport ", e);
		}finally {
			IOUtils.closeQuietly(os);
		}
	}
	/**
	 *
	 * @Method Name  : gridDownload
	 * @Method 설명 : 그리드 데이터 다운로드.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param sqlGridDownloadInfo
	 * @param res
	 * @throws IOException
	 */
	public void gridDownload(SqlGridDownloadInfo sqlGridDownloadInfo, HttpServletResponse res) throws IOException {
		String exportType = sqlGridDownloadInfo.getExportType();

		List<GridColumnInfo> columnInfo = Arrays.asList(VartechUtils.jsonStringToObject(sqlGridDownloadInfo.getHeaderInfo(), GridColumnInfo[].class , true));

		//List<Map> downloadData = VartechUtils.jsonStringToObject(sqlGridDownloadInfo.getGridData(), ArrayList.class);

		logger.info("grid download : {} " , sqlGridDownloadInfo);


		String downloadName = "grid-data-download";

		OutputStream os = res.getOutputStream();

		try {
			String jsonString = sqlGridDownloadInfo.getGridData();
			if("csv".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".csv",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToCsv(jsonString, columnInfo, os);
			}else if("json".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".json",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToJson(jsonString, columnInfo, os);
			}else if("xml".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".xml",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToXml(jsonString, columnInfo , os);
			}else if("excel".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".xlsx",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToExcel(jsonString, columnInfo , os);
			}

			if(os !=null) os.close();
		}catch(Exception e) {
			logger.error(" param {} ", sqlGridDownloadInfo);
			logger.error(" gridDownload {}", e.getMessage());
	    	logger.error(" gridDownload ", e);
		}finally {
			IOUtils.closeQuietly(os);
		}
	}
}