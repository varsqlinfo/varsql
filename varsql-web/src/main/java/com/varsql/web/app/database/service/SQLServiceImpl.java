package com.varsql.web.app.database.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.sql.Connection;
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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.ColumnJavaType;
import com.varsql.core.common.constants.SqlDataConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.GridUtils;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.data.writer.SQLWriter;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.ResultSetConvertException;
import com.varsql.core.sql.SqlExecuteManager;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.beans.SqlExecuteDTO;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.executor.SQLExecuteResult;
import com.varsql.core.sql.executor.SelectExecutor;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.sql.SqlGridDownloadInfo;
import com.varsql.web.dto.sql.SqlLogInfoDTO;
import com.varsql.web.exception.DataDownloadException;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.entity.sql.SqlStatisticsEntity;
import com.varsql.web.util.ConvertUtils;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.ValidateUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.excel.ExcelCellMetaInfo;
import com.vartech.common.excel.ExcelCellStyle;
import com.vartech.common.io.writer.AbstractWriter;
import com.vartech.common.io.writer.CSVWriter;
import com.vartech.common.io.writer.ExcelWriter;
import com.vartech.common.io.writer.JSONWriter;
import com.vartech.common.io.writer.WriteMetadataInfo;
import com.vartech.common.io.writer.XMLWriter;
import com.vartech.common.report.ExcelConstants;
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
	private CommonLogService commonLogService;
	
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

		DBVenderType dbType = DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType());

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
	public ResponseResult sqlData(SqlExecuteDTO sqlExecuteInfo, String ip) throws Exception {

		Map sqlParamMap = VartechUtils.jsonStringToObject(sqlExecuteInfo.getSqlParam(), HashMap.class);

		DatabaseInfo dbinfo = sqlExecuteInfo.getDatabaseInfo();

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(sqlExecuteInfo.getSql(), sqlParamMap, DBVenderType.getDBType( dbinfo.getType() ));

		List<SqlSource> sqlList = parseInfo.getList();

		int limit = sqlExecuteInfo.getLimit();

		if(!SecurityUtil.isAdmin()) {
			sqlExecuteInfo.setLimit(limit > dbinfo.getMaxSelectCount() ? dbinfo.getMaxSelectCount() : limit);
		}

		ArrayList<SqlSourceResultVO> reLst = new ArrayList<SqlSourceResultVO>();

		ResponseResult result = new ResponseResult();

		Connection conn = null;
		SqlSourceResultVO ssrv =null;
		
		String vconnid = sqlExecuteInfo.getDatabaseInfo().getVconnid(); 

		long stddt = System.currentTimeMillis();
		String[] mmddHH = VarsqlDateUtils.format("MM-dd-HH", stddt).split("-");

		SqlLogInfoDTO sqlLogInfo= new SqlLogInfoDTO();
		sqlLogInfo.setVconnid(vconnid);
		sqlLogInfo.setViewid(SecurityUtil.userViewId());
		sqlLogInfo.setStartTime(stddt);

		sqlLogInfo.setSMm(Integer.valueOf(mmddHH[0]));
		sqlLogInfo.setSDd(Integer.valueOf(mmddHH[1]));
		sqlLogInfo.setSHh(Integer.valueOf(mmddHH[2]));

		sqlLogInfo.setUsrIp(ip);

		SqlSource tmpSqlSource =null;
		int sqldx =0,sqlSize = sqlList.size();

		String errorMsg = "";
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			
			if(!StringUtils.isBlank(sqlExecuteInfo.get_requid_())) {
				SqlExecuteManager.getInstance().setStatementInfo(sqlExecuteInfo.get_requid_(), null);
	    	}
			
			conn.setAutoCommit(false);

			List<SqlStatisticsEntity> allSqlStatistics = new LinkedList<SqlStatisticsEntity>();
			for (sqldx =0;sqldx <sqlSize; sqldx++) {
				tmpSqlSource = sqlList.get(sqldx);

				ssrv = new SqlSourceResultVO();
				reLst.add(ssrv);
				tmpSqlSource.setResult(ssrv);
				ssrv.setStarttime(System.currentTimeMillis());

				ssrv = SQLUtils.getSqlExecute(sqlExecuteInfo, conn, tmpSqlSource, true);

				ssrv.setEndtime(System.currentTimeMillis());
				ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
				ssrv.setResultMessage((ssrv.getDelay())+" SECOND : "+StringUtils.escape(ssrv.getResultMessage(), EscapeType.html));

				sqlLogInfo.setStartTime(ssrv.getStarttime());
				sqlLogInfo.setCommandType(tmpSqlSource.getCommand().getCommandName());
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
			
			try {
				commonLogService.sqlLogInsert(allSqlStatistics);
			}catch(Exception e) {
				logger.warn("sql log insert error : {}", e.getMessage());
			}
			
			result.setList(reLst);
			conn.commit();
		} catch (Throwable e ) {
			
			if (conn != null && !conn.isClosed()) conn.rollback(); 

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

			result.addCustomMapAttribute("errorLine", sqldx);
			result.setItemOne(tmpSqlSource==null?sqlList.get(0):tmpSqlSource);

			LoggerFactory.getLogger("sqlErrorLog").error("sqlData errorLine : {}", sqldx,e);

		}finally{
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				JdbcUtils.close(conn);
			}
		}
		
		if(!StringUtils.isBlank(sqlExecuteInfo.get_requid_())) {
			SqlExecuteManager.getInstance().removeStatementInfo(sqlExecuteInfo.get_requid_());
		}

		long enddt = System.currentTimeMillis();

		try {
			commonLogService.saveSqlHistory(SqlHistoryEntity.builder()
				.vconnid(sqlLogInfo.getVconnid())
				.viewid(sqlLogInfo.getViewid())
				.startTime(ConvertUtils.longToTimestamp(stddt))
				.endTime(ConvertUtils.longToTimestamp(enddt))
				.delayTime((int) ((enddt- stddt)/1000))
				.logSql(sqlExecuteInfo.getSql())
				.usrIp(sqlLogInfo.getUsrIp())
				.errorLog(errorMsg)
				.build());
		}catch(Exception e) {
			logger.warn("save history error : {}", e.getMessage());
		}

		return  result;
	}

	/**
	 * 데이터 내보내기.
	 * @param paramMap
	 * @param sqlExecuteInfo
	 * @param req
	 * @param res
	 */
	@SuppressWarnings("rawtypes")
	public void dataExport(DataMap paramMap, SqlExecuteDTO sqlExecuteInfo, HttpServletRequest req, HttpServletResponse res) throws Exception {

		String objectName = sqlExecuteInfo.getObjectName();

		if(!sqlExecuteInfo.getDatabaseInfo().getSchema().equals(sqlExecuteInfo.getSchema())) {
			objectName = sqlExecuteInfo.getSchema()+"."+objectName;
		}

		StringBuilder reqQuerySb= new StringBuilder().append("select * from ").append(objectName).append(" where 1=1 ");

		String conditionQuery = paramMap.getString("conditionQuery");
		if(!StringUtils.isBlank(conditionQuery)) {
			conditionQuery = StringUtils.lTrim(conditionQuery);
			if(conditionQuery.startsWith("where")) {
				conditionQuery = conditionQuery.replaceFirst("where", "");
			}

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

			String downloadFilePath = FileUtils.pathConcat(fileExportPath.toAbsolutePath().toString(), exportType.concatExtension(VartechUtils.generateUUID()));
			outstream = new FileOutputStream(downloadFilePath);

			if(VarsqlFileType.CSV.equals(exportType)){
				writer = new  CSVWriter(outstream, ',' , exportCharset);
			}else if(VarsqlFileType.JSON.equals(exportType)){
				writer = new JSONWriter(outstream, exportCharset);
			}else if(VarsqlFileType.XML.equals(exportType)){
				writer = new XMLWriter(outstream, "row" , exportCharset);
			}else if(VarsqlFileType.EXCEL.equals(exportType)){
				sqlExecuteInfo.setUseColumnAlias(false);
				writer = new ExcelWriter(outstream);
			}else {
				writer = new SQLWriter(outstream, DBVenderType.getDBType(sqlExecuteInfo.getDatabaseInfo().getType()), objectName, exportCharset);
			}

			logger.debug("data export downloadFilePath :{} , query : {}", downloadFilePath, sqlExecuteInfo.getSql());

			final String tableName =objectName;

			SQLExecuteResult ser = new SelectExecutor().execute(sqlExecuteInfo, new SelectExecutorHandler(writer) {
				private boolean firstFlag = true;

				@Override
				public boolean handle(SelectInfo handleParam) {
					if(firstFlag) {

						WriteMetadataInfo whi = new WriteMetadataInfo("exportInfo");

						List<ExportColumnInfo> columns = new LinkedList<ExportColumnInfo>();

						handleParam.getColumnInfoList().forEach(item->{
							ExportColumnInfo eci = new ExportColumnInfo();
							
							eci.setName(item.getLabel());
							eci.setAlias(item.getKey());
							eci.setType(item.getDbType());
							eci.setTypeCode(item.getDbTypeCode());
							eci.setNumber(item.isNumber());
							eci.setLob(item.isLob());

							columns.add(eci);
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

			VarsqlUtils.setResponseDownAttr(res, req, exportType.concatExtension(exportFileName));

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
	 * @param req 
	 * @param res
	 * @throws IOException
	 */
	public void gridDownload(SqlGridDownloadInfo sqlGridDownloadInfo, HttpServletRequest req, HttpServletResponse res) throws IOException {
		VarsqlFileType exportType = sqlGridDownloadInfo.getExportType();

		String exportCharset = VarsqlConstants.CHAR_SET;
		String downloadName = "varsql-select-data";

		AbstractWriter writer = null;
		JsonParser parser =null;

		try (OutputStream outstream = res.getOutputStream()){

			List<GridColumnInfo> columnInfos = VartechUtils.jsonStringToObject(sqlGridDownloadInfo.getHeaderInfo(), new TypeReference<LinkedList<GridColumnInfo>>() {} , true);
			
			if(VarsqlFileType.CSV.equals(exportType)){
				writer = new  CSVWriter(outstream, ',' , exportCharset);
			}else if(VarsqlFileType.JSON.equals(exportType)){
				writer = new JSONWriter(outstream, exportCharset);
			}else if(VarsqlFileType.XML.equals(exportType)){
				writer = new XMLWriter(outstream, "row" , exportCharset);
			}else if(VarsqlFileType.EXCEL.equals(exportType)){
				
				int columnInfoSize= columnInfos.size(); 
	        	
	        	ExcelCellMetaInfo[] excelCellMetaInfo = new ExcelCellMetaInfo[columnInfoSize];
	        	
	        	for (int i = 0; i < columnInfoSize; i++) {
	        		GridColumnInfo item = columnInfos.get(i);
	        		excelCellMetaInfo[i] = new ExcelCellMetaInfo(item.getLabel(), item.getLabel(), 10, ExcelConstants.ALIGN.getAlign(item.getAlign()));
				}

				writer = new ExcelWriter(outstream, "data", excelCellMetaInfo);
			}

			VarsqlUtils.setResponseDownAttr(res, req, exportType.concatExtension(downloadName));

			
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

	        			if(valueToken==null || JsonToken.VALUE_NULL.equals(valueToken)) {
	        				rowInfo.put(columnInfo.getLabel(), null);
	        				continue; 
	        			}
	        			
	        			if(JsonToken.VALUE_FALSE.equals(valueToken) || JsonToken.VALUE_TRUE.equals(valueToken)){
	        				rowInfo.put(columnInfo.getLabel(), parser.getBooleanValue());
	        				continue; 
	        			}
	        			
	        			if(JsonToken.VALUE_NUMBER_INT.equals(valueToken) || JsonToken.VALUE_NUMBER_FLOAT.equals(valueToken)){
	        				if(ColumnJavaType.DECIMAL.equals(columnInfo.getType())) {
	        					rowInfo.put(columnInfo.getLabel(), parser.getDecimalValue());
	        				}else if(ColumnJavaType.FLOAT.equals(columnInfo.getType())) {
	        					rowInfo.put(columnInfo.getLabel(), parser.getFloatValue());
	        				}else if(ColumnJavaType.DOUBLE.equals(columnInfo.getType())) {
	        					rowInfo.put(columnInfo.getLabel(), parser.getDoubleValue());
	        				}else {
	        					rowInfo.put(columnInfo.getLabel(), parser.getNumberValue());
	        				}
		        			
	        				continue; 
	        			}
	        			
		        		rowInfo.put(columnInfo.getLabel(), parser.getText());
		        		
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