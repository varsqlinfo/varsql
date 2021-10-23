package com.varsql.web.app.database.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.GridUtils;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.data.writer.SQLWriter;
import com.varsql.core.db.DBType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.ResultSetConvertException;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.builder.VarsqlCommandType;
import com.varsql.core.sql.builder.VarsqlStatementType;
import com.varsql.core.sql.executor.SQLExecuteResult;
import com.varsql.core.sql.executor.SelectExecutor;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;
import com.varsql.core.sql.executor.handler.SQLHandlerParameter;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.mapping.ParameterMapping;
import com.varsql.core.sql.mapping.ParameterMode;
import com.varsql.core.sql.type.SQLDataType;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLParamUtils;
import com.varsql.core.sql.util.SQLResultSetUtils;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.sql.SqlExecuteDTO;
import com.varsql.web.dto.sql.SqlGridDownloadInfo;
import com.varsql.web.dto.sql.SqlLogInfoDTO;
import com.varsql.web.exception.DataDownloadException;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.repository.sql.SqlHistoryEntityRepository;
import com.varsql.web.repository.sql.SqlStatisticsEntityRepository;
import com.varsql.web.util.ConvertUtils;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.ValidateUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.io.writer.AbstractWriter;
import com.vartech.common.io.writer.CSVWriter;
import com.vartech.common.io.writer.ExcelWriter;
import com.vartech.common.io.writer.JSONWriter;
import com.vartech.common.io.writer.WriteMetadataInfo;
import com.vartech.common.io.writer.XMLWriter;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.IOUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.StringUtils.EscapeType;
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
	 * @param sqlExecuteInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseResult sqlFormat(SqlExecuteDTO sqlExecuteInfo) throws Exception {
		ResponseResult result =new ResponseResult();

		DBType dbType = DBType.getDBType(sqlExecuteInfo.getDbType());

		if("varsql".equals(sqlExecuteInfo.getCustom().get("formatType"))){
			result.setItemOne(VarsqlFormatterUtil.format(sqlExecuteInfo.getSql(),dbType , VarsqlFormatterUtil.FORMAT_TYPE.VARSQL));
		}else{
			result.setItemOne(VarsqlFormatterUtil.format(sqlExecuteInfo.getSql(),dbType ));
		}

		return result;

	}

	/**
	 *
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 데이터 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 9.
	 * @변경이력  :
	 * @param sqlExecuteInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseResult sqlData(SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req) throws Exception {

		Map sqlParamMap = VartechUtils.jsonStringToObject(sqlExecuteInfo.getSqlParam(), HashMap.class);

		DatabaseInfo dbinfo = SecurityUtil.userDBInfo(sqlExecuteInfo.getConuid());

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(sqlExecuteInfo.getSql(), sqlParamMap, DBType.getDBType(sqlExecuteInfo.getDbType()));

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

			List<SqlStatisticsEntity> allSqlStatistics = new LinkedList<SqlStatisticsEntity>();
			for (sqldx =0;sqldx <sqlSize; sqldx++) {
				tmpSqlSource = sqlList.get(sqldx);

				ssrv = new SqlSourceResultVO();
				reLst.add(ssrv);
				tmpSqlSource.setResult(ssrv);
				ssrv.setStarttime(System.currentTimeMillis());

				getRequestSqlData(sqlExecuteInfo, conn, tmpSqlSource, dbinfo, true);

				ssrv.setEndtime(System.currentTimeMillis());
				ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
				ssrv.setResultMessage((ssrv.getDelay())+" SECOND : "+StringUtils.escape(ssrv.getResultMessage(), EscapeType.html));

				sqlLogInfo.setStartTime(ssrv.getStarttime());
				sqlLogInfo.setCommandType(tmpSqlSource.getCommandType());
				sqlLogInfo.setEndTime(ssrv.getEndtime());

				allSqlStatistics.add(SqlStatisticsEntity.builder()
					.vconnid(sqlLogInfo.getVconnid())
					.viewid(sqlLogInfo.getViewid())
					.startTime(ConvertUtils.longToLocalDateTime(sqlLogInfo.getStartTime()))
					.endTime(ConvertUtils.longToLocalDateTime(sqlLogInfo.getEndTime()))
					.delayTime(sqlLogInfo.getDelayTime())
					.sMm(sqlLogInfo.getSMm())
					.sDd(sqlLogInfo.getSDd())
					.sHh(sqlLogInfo.getSHh())
					.resultCount(sqlLogInfo.getResultCount())
					.commandType(sqlLogInfo.getCommandType())
					.build());

				if(SqlDataConstants.VIEWTYPE.GRID.val().equals(ssrv.getViewType())) {
					break;
				}
			}

			sqlLogInsert(allSqlStatistics);

			result.setItemList(reLst);
			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();

			errorMsg = e.getMessage();

			if(e instanceof ResultSetConvertException){
				result.setResultCode(VarsqlAppCode.EC_SQL_RESULT_CONVERT);
				ssrv= ((ResultSetConvertException)e).getSsrv();

				if(ssrv != null) {
					ssrv= new SqlSourceResultVO();
				}
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
			}else {
				boolean ssrvNullFlag = false;
				if(ssrv==null) {
					ssrvNullFlag = true;
					ssrv = new SqlSourceResultVO();
				}

				ssrv.setEndtime(System.currentTimeMillis());
				String tmpMsg = parseInfo.getMessage();
				tmpMsg = (tmpMsg  == null || "".equals(tmpMsg) ?"" :StringUtils.escape(parseInfo.getMessage(), EscapeType.html)+"<br/>");

				if(e instanceof ConnectionFactoryException) {
					if(((ConnectionFactoryException)e).getErrorCode() == VarsqlAppCode.EC_DB_POOL_CLOSE) {
						result.setResultCode(VarsqlAppCode.EC_DB_POOL_CLOSE);
					}else {
						result.setResultCode(VarsqlAppCode.EC_DB_POOL);
					}
				}else {
					result.setResultCode(VarsqlAppCode.EC_SQL);
				}

				result.setMessage(tmpMsg+StringUtils.escape(ssrv.getResultMessage(), EscapeType.html));

				if(ssrvNullFlag) {
					result.setMessage(errorMsg);
				}
			}

			result.addCustoms("errorLine", sqldx);
			result.setItemOne(tmpSqlSource==null?sqlList.get(0):tmpSqlSource);

			if(VarsqlUtils.isRuntimelocal()) {
				logger.error("sqlData : {} ", e.getMessage(), e);
			}
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				JdbcUtils.close(conn);
			}
		}

		long enddt = System.currentTimeMillis();

		saveSqlHistory(SqlHistoryEntity.builder()
				.vconnid(sqlLogInfo.getVconnid())
				.viewid(sqlLogInfo.getViewid())
				.startTime(ConvertUtils.longToTimestamp(stddt))
				.endTime(ConvertUtils.longToTimestamp(enddt))
				.delayTime((int) ((enddt- stddt)/1000))
				.logSql(sqlExecuteInfo.getSql())
				.usrIp(sqlLogInfo.getUsrIp())
				.errorLog(errorMsg)
				.build());

		return  result;
	}

	/**
	 *
	 * @Method Name  : saveSqlHistory
	 * @Method 설명 : sql history 저장.
	 * @작성일   : 2020. 11. 04.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param stmt
	 * @param maxRow
	 * @throws SQLException
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	private void saveSqlHistory(SqlHistoryEntity sqlHistoryEntity) {
		try {
			sqlHistoryEntityRepository.save(sqlHistoryEntity);
		}catch(Throwable e) {
			logger.error(" sqlData sqlHistoryEntity : ", e);
		}
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
	 * @Method 설명 : 데이터 얻기
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

		        SQLParamUtils.setCallableParameter(callStatement, tmpSqlSource);
		        setMaxRow(callStatement, maxRow);
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
		        	return ;
		        }else if(hasResults) {
		        	rs = (ResultSet)callStatement.getObject(cursorObjIdx);

		        	if(rs != null) {
			        	SQLResultSetUtils.resultSetHandler(rs, ssrv, sqlExecuteInfo, dbInfo, maxRow, gridKeyAlias);
			            ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
			            ssrv.setResultMessage(String.format("select count : %s ", new Object[] { Long.valueOf(ssrv.getResultCnt()) }));
		        	}else {
		        		ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
			            ssrv.setResultMessage("Cursor is null");
		        	}

		            return ;
		        }
		        stmt = callStatement;
			}else{
				PreparedStatement pstmt = conn.prepareStatement(tmpSqlSource.getQuery());
				SQLParamUtils.setSqlParameter(pstmt, tmpSqlSource);
				setMaxRow(pstmt, maxRow);
				hasResults = pstmt.execute();

				stmt= pstmt;
			}

			if(hasResults) {
				rs = stmt.getResultSet();
				SQLResultSetUtils.resultSetHandler(rs, ssrv, sqlExecuteInfo, dbInfo, maxRow, gridKeyAlias);
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
				ssrv.setResultMessage(String.format("select count : %s ", ssrv.getResultCnt()));
			}else{
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
				ssrv.setResultCnt(stmt.getUpdateCount());

				if(VarsqlCommandType.isUpdateCountCommand(tmpSqlSource.getCommandType())) {
					ssrv.setResultMessage(String.format("%s count : %s", tmpSqlSource.getCommandType(), ssrv.getResultCnt()));
				}else {
					ssrv.setResultMessage(String.format("%s success", tmpSqlSource.getCommandType()));
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
	    	JdbcUtils.close(stmt, rs);
	    }
	}

	/**
	 *
	 * @Method Name  : sqlLogInsert
	 * @Method 설명 : 사용자 sql 로그 저장
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param tmpSqlSource
	 * @param ssrv
	 */
	@Async(ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
	private void sqlLogInsert(List<SqlStatisticsEntity> allSqlStatistics) {
		try{
			if(allSqlStatistics.size() == 1) {
				sqlStatisticsEntityRepository.save(allSqlStatistics.get(0));
			}else if(allSqlStatistics.size() > 1) {
				sqlStatisticsEntityRepository.saveAll(allSqlStatistics);
			}
	    }catch(Exception e){
	    	logger.error(" sqlLogInsert {}", e.getMessage() , e);
	    }
	}

	/**
	 * 데이터 내보내기.
	 * @param paramMap
	 */
	@SuppressWarnings("rawtypes")
	public void dataExport(ParamMap paramMap, SqlExecuteDTO sqlExecuteInfo, HttpServletResponse res) throws Exception {

		String objectName = sqlExecuteInfo.getObjectName();

		if(!sqlExecuteInfo.getBaseSchema().equals(sqlExecuteInfo.getSchema())) {
			objectName = sqlExecuteInfo.getSchema()+"."+objectName;
		}

		StringBuilder reqQuerySb= new StringBuilder().append("select * from ").append(objectName).append(" where 1=1 ");

		String conditionQuery = paramMap.getString("conditionQuery");
		if(!StringUtils.isBlank(conditionQuery)) {
			conditionQuery = StringUtils.lTrim(conditionQuery);
			if(conditionQuery.startsWith("where")) {
				conditionQuery = conditionQuery.replaceFirst("where", "");
			}

			if(conditionQuery.startsWith("and")) {
				conditionQuery = conditionQuery.replaceFirst("and", "");
			}

			conditionQuery =  " and " +conditionQuery;

			reqQuerySb.append(conditionQuery);
		}

		sqlExecuteInfo.setSqlParam("{}");
		sqlExecuteInfo.setSql(reqQuerySb.toString());

		String exportCharset = paramMap.getString("charset", VarsqlConstants.CHAR_SET);

		OutputStream outstream = null;
		AbstractWriter writer = null;

		try {

			VarsqlFileType exportType = sqlExecuteInfo.getExportType();

			Path fileExportPath = FileServiceUtils.getSavePath(UploadFileType.EXPORT);

			String downloadFilePath = FileUtils.pathConcat(fileExportPath.toAbsolutePath().toString(), VartechUtils.generateUUID()+ exportType.getExtension());
			outstream = new FileOutputStream(downloadFilePath);

			if(VarsqlFileType.CSV.equals(exportType)){
				writer = new  CSVWriter(outstream, ',' , exportCharset);
			}else if(VarsqlFileType.JSON.equals(exportType)){
				writer = new JSONWriter(outstream, "row", exportCharset);
			}else if(VarsqlFileType.XML.equals(exportType)){
				writer = new XMLWriter(outstream, "row" , exportCharset);
			}else if(VarsqlFileType.EXCEL.equals(exportType)){
				writer = new ExcelWriter(outstream);
			}else {
				writer = new SQLWriter(outstream, DBType.getDBType(sqlExecuteInfo.getDbType()), objectName, exportCharset);
			}

			logger.debug("data export downloadFilePath :{} , query : {}", downloadFilePath, sqlExecuteInfo.getSql());

			final String tableName =objectName;

			SQLExecuteResult ser = new SelectExecutor().execute(sqlExecuteInfo, new AbstractSQLExecutorHandler(writer) {
				private boolean firstFlag = true;

				@Override
				public boolean handle(SQLHandlerParameter handleParam) {
					if(firstFlag) {

						WriteMetadataInfo whi = new WriteMetadataInfo("exportInfo");

						List<ExportColumnInfo> columns = new LinkedList<ExportColumnInfo>();

						handleParam.getColumnInfoList().forEach(item->{
							ExportColumnInfo gci = new ExportColumnInfo();

							gci.setName(item.getLabel());
							gci.setType(item.getDbType());
							gci.setNumber(item.isNumber());
							gci.setLob(item.isLob());

							columns.add(gci);
						});

						whi.addMetedata("tableName", tableName);
						whi.addMetedata("columns", columns);

						getWriter().setMetadata(whi);

						if(VarsqlFileType.SQL.equals(exportType)) {
							((SQLWriter)getWriter()).setColumnInfos(handleParam.getColumnInfoList());
						}
						firstFlag =false;
					}

					try {
						getWriter().addRow(handleParam.getRowObject());
					} catch (IOException e) {
						logger.error(e.getMessage() , e);
						return false;
					}
					return true;
				}
			});

			writer.writeAndClose();

			if(ser.getResultCode() != null) {
				throw new DataDownloadException(ser.getResultCode(), ser.getMessage(), new VarsqlAppException(ser.getMessage()));
			}

			String exportFileName = ValidateUtils.getValidFileName(paramMap.getString("fileName", objectName));

			VarsqlUtils.setResponseDownAttr(res, exportFileName + exportType.getExtension());

			try(FileInputStream fileInputStream  = new FileInputStream(new File(downloadFilePath));
				OutputStream downloadStream = res.getOutputStream();)
			{
				byte[] buf = new byte[8192];

				int read = 0;
		        while ((read = fileInputStream.read(buf)) != -1){
		        	downloadStream.write(buf, 0, read);
		        }
		        downloadStream.flush();

		        downloadStream.close();
		        fileInputStream.close();
			}

		}catch(Exception e) {
			throw e;
		}finally {
			IOUtils.close(writer);
			IOUtils.close(outstream);
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
		VarsqlFileType exportType = sqlGridDownloadInfo.getExportType();

		String exportCharset = VarsqlConstants.CHAR_SET;
		String downloadName = "varsql-select-data";

		AbstractWriter writer = null;
		JsonParser parser =null;

		try (OutputStream outstream = res.getOutputStream()){

			if(VarsqlFileType.CSV.equals(exportType)){
				writer = new  CSVWriter(outstream, ',' , exportCharset);
			}else if(VarsqlFileType.JSON.equals(exportType)){
				writer = new JSONWriter(outstream, "row", exportCharset);
			}else if(VarsqlFileType.XML.equals(exportType)){
				writer = new XMLWriter(outstream, "row" , exportCharset);
			}else if(VarsqlFileType.EXCEL.equals(exportType)){
				writer = new ExcelWriter(outstream);
			}

			VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + exportType.getExtension(), VarsqlConstants.CHAR_SET));

			List<GridColumnInfo> columnInfos = VartechUtils.jsonStringToObject(sqlGridDownloadInfo.getHeaderInfo(), new TypeReference<LinkedList<GridColumnInfo>>() {} , true);
			logger.debug("grid download : {} " , columnInfos);

			Map<String,GridColumnInfo> gridColumnInfoMap = GridUtils.getKeyMap(columnInfos);

			parser =  new JsonFactory().createParser(sqlGridDownloadInfo.getGridData());
	        parser.nextToken();                          //start reading the file
	        Map<String,Object> rowInfo;
	        GridColumnInfo columnInfo;
	        JsonToken valueToken;
	        while (parser.nextToken() != JsonToken.END_ARRAY) {    //loop until "}"
	        	rowInfo = new LinkedHashMap<>();
	        	while (parser.nextToken() != JsonToken.END_OBJECT) {

	        		columnInfo = GridUtils.getGridInfoForKey(parser.getCurrentName(), gridColumnInfoMap);

	        		if(columnInfo != null) {
	        			parser.nextToken();

	        			valueToken = parser.currentToken();

	        			if(valueToken==null) {
	        				rowInfo.put(columnInfo.getLabel(), null);
	        			}else {
	        				if(columnInfo.isNumber() && valueToken.isNumeric()) {
		        				rowInfo.put(columnInfo.getLabel(), parser.getNumberValue());
		        			}else{
		        				rowInfo.put(columnInfo.getLabel(), parser.getText());
		        			}
	        			}
	        		}
	        	}
	        	writer.addRow(rowInfo);
	        }
	        writer.writeAndClose();
	        parser.close();

			if(outstream !=null) outstream.close();
		}catch(Exception e) {
			logger.error(" param {} ", sqlGridDownloadInfo);
			logger.error(" gridDownload {}", e.getMessage(), e);
		}finally {
			IOUtils.close(writer);
			if(parser!=null)parser.close();
		}

	}
}