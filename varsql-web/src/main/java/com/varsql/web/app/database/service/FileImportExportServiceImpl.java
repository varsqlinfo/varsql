package com.varsql.web.app.database.service;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.core.common.beans.ProgressInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.job.JobExecuteResult;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.executor.FileImportExecutor;
import com.varsql.core.sql.executor.UpdateExecutor;
import com.varsql.core.sql.executor.handler.UpdateExecutorHandler;
import com.varsql.core.sql.executor.handler.UpdateInfo;
import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.dto.file.FileImportInfo;
import com.varsql.web.dto.file.FileImportResult;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.app.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: FileImportExportServiceImpl.java
* @DESC		: file import export service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
@RequiredArgsConstructor
public class FileImportExportServiceImpl{
	private final Logger logger = LoggerFactory.getLogger(FileImportExportServiceImpl.class);

	private final FileInfoEntityRepository fileInfoEntityRepository;

	/**
	 *
	 * @param req 
	 * @Method Name  : importFile
	 * @Method 설명 : import file
	 * @작성자   : ytkim
	 * @작성일   : 2020. 11. 22.
	 * @변경이력  :
	 */
	public ResponseResult importFile(FileImportInfo fii, HttpServletRequest req) {
			
		String conuid  = fii.getConuid();
		String importType = fii.getImportType();

		List<FileInfoEntity> importFileList = fileInfoEntityRepository.findAllById(Arrays.asList(FileUtils.getFileIds(fii.getFileIds())));

		ResponseResult result = new ResponseResult();
		List<FileImportResult> resultInfo = new ArrayList<>();
		
		String progressUid = HttpUtils.getString(req, "progressUid");
		String sessAttrKey = HttpSessionConstants.progressKey(progressUid);
		HttpSession session = req.getSession();
		
		ProgressInfo progressInfo = new ProgressInfo();
		progressInfo.setTotalItemSize(importFileList.size());
		
		session.setAttribute(sessAttrKey, progressInfo);
		
		int itemIdx=0;
		for(FileInfoEntity fileInfo : importFileList){
			
			progressInfo.setName(fileInfo.getFileName());
			progressInfo.setItemIdx(++itemIdx);
			progressInfo.setProgressContentLength(0);
			
			FileImportResult fileImportResult = new FileImportResult();
			try {
				fileImportResult.setFileId(fileInfo.getFileId());
				fileImportResult.setFileName(fileInfo.getFileName());
				JobExecuteResult jer =null;
				if("sql".equals(importType)) {
					jer = sqlImport(fileInfo, conuid, progressInfo);
				}else if("xml".equals(importType)) {
					jer = xmlImport(fileInfo, conuid, progressInfo);
				}else if("json".equals(importType)) {
					jer = jsonImport(fileInfo, conuid, progressInfo);
				}else if("csv".equals(importType)) {
					jer = csvImport(fileInfo, conuid, progressInfo);
				}

				if(jer.getResultCode() != null) {
					fileImportResult.setResultCode(jer.getResultCode());
				}
				fileImportResult.setResultCount(jer.getExecuteCount());
				fileImportResult.setMessage(jer.getMessage());

			} catch (IOException | SQLException e) {
				fileImportResult.setResultCode(VarsqlAppCode.ERROR);
				fileImportResult.setMessage(e.getMessage());
				logger.error("file import error importType : {} , conuid : {} , fileInfo :{} " ,importType, fileInfo);
				logger.error("error message : {}" ,e.getMessage(), e);
			}
			
			progressInfo.addItem(fileImportResult);

			resultInfo.add(fileImportResult);
		}
		
		session.setAttribute(sessAttrKey, "complete");

		result.setList(resultInfo);

		return result;
	}

	/**
	 * @method  : sqlImport
	 * @desc : sql import
	 * @author   : ytkim
	 * @date   : 2021. 5. 23.
	 * @param fileInfo
	 * @param conuid
	 * @param progressInfo 
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private JobExecuteResult sqlImport(FileInfoEntity fileInfo, String conuid, ProgressInfo progressInfo) throws IOException, SQLException {
		SqlStatementInfo ssi = new SqlStatementInfo();
		ssi.setDatabaseInfo(SecurityUtil.userDBInfo(conuid));
		ssi.setSqlParam("{}");

		String insertQuery = FileUtils.readFileToString(FileServiceUtils.getFileInfoToFile(fileInfo));
		ssi.setSql(insertQuery);

		JobExecuteResult jer =new UpdateExecutor().execute(ssi, new UpdateExecutorHandler() {
			private int idx =0; 
			@Override
			public boolean handle(UpdateInfo sqlResultVO) {
				++idx;
				progressInfo.setProgressContentLength(idx);
				return true;
			}
		});

		return jer;
	}

	/**
	 * @method  : csvImport
	 * @desc : csv import
	 * @author   : ytkim
	 * @date   : 2021. 5. 23.
	 * @param fileInfo
	 * @param conuid
	 * @return
	 * @throws IOException
	 */
	private JobExecuteResult csvImport(FileInfoEntity fileInfo, String conuid, ProgressInfo progressInfo) throws IOException {


		try(LineIterator it = org.apache.commons.io.FileUtils.lineIterator(FileServiceUtils.getFileInfoToFile(fileInfo), VarsqlConstants.CHAR_SET);) {
		    while (it.hasNext()) {
		        String line = it.nextLine();
		        //line
		    }
		}
		return null;
	}


	private JobExecuteResult jsonImport(FileInfoEntity fileInfo, String conuid, ProgressInfo progressInfo) throws SQLException {

		SqlStatementInfo ssi = new SqlStatementInfo();
		ssi.setDatabaseInfo(SecurityUtil.userDBInfo(conuid));
		ssi.setExportType(VarsqlFileType.JSON.name());
		ssi.addCustom(FileImportExecutor.IMPORT_FILE_PARAM_NAME, FileServiceUtils.getPath(fileInfo.getFilePath()).toUri());

		JobExecuteResult jer =new FileImportExecutor().execute(ssi, new UpdateExecutorHandler() {
			private int idx =0; 
			@Override
			public boolean handle(UpdateInfo sqlResultVO) {
				++idx;
				progressInfo.setProgressContentLength(idx);
				return true;
			}
		});
		return jer;
	}

	private JobExecuteResult xmlImport(FileInfoEntity fileInfo, String conuid, ProgressInfo progressInfo) throws SQLException {
		SqlStatementInfo ssi = new SqlStatementInfo();
		ssi.setDatabaseInfo(SecurityUtil.userDBInfo(conuid));
		ssi.setExportType(VarsqlFileType.XML.name());
		ssi.addCustom(FileImportExecutor.IMPORT_FILE_PARAM_NAME, FileServiceUtils.getPath(fileInfo.getFilePath()).toUri());

		JobExecuteResult jer =new FileImportExecutor().execute(ssi, new UpdateExecutorHandler() {
			private int idx =0; 
			@Override
			public boolean handle(UpdateInfo sqlResultVO) {
				++idx;
				progressInfo.setProgressContentLength(idx);
				return true;
			}
		});
		return jer;
	}


}