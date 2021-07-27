package com.varsql.web.app.database.service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.util.CommUtils;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.executor.SQLExecuteResult;
import com.varsql.core.sql.executor.UpdateExecutor;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;
import com.varsql.core.sql.executor.handler.SQLHandlerParameter;
import com.varsql.web.dto.file.FileImportResult;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.user.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.FileUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ExportServiceImpl.java
* @DESC		: export service
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class FileImportExportServiceImpl{
	private final Logger logger = LoggerFactory.getLogger(FileImportExportServiceImpl.class);

	@Autowired
	private FileInfoEntityRepository fileInfoEntityRepository;

	/**
	 *
	 * @Method Name  : importFile
	 * @Method 설명 : import file
	 * @작성자   : ytkim
	 * @작성일   : 2020. 11. 22.
	 * @변경이력  :
	 */
	public ResponseResult importFile(String conuid, String importType, String fileIds) {

		List<FileInfoEntity> importFileList = fileInfoEntityRepository.findAllById(Arrays.asList(CommUtils.getFileIds(fileIds)));

		ResponseResult result = new ResponseResult();
		List<FileImportResult> resultInfo = new ArrayList<>();

		importFileList.forEach(fileInfo ->{
			FileImportResult fileImportResult = new FileImportResult();
			try {
				fileImportResult.setFileName(fileInfo.getFileName());
				SQLExecuteResult ser =null;
				if("1".equals(importType)) {
					ser = sqlImport(fileInfo, conuid);
				}else if("2".equals(importType)) {
					ser = jsonXmlImport(fileInfo, conuid);
				}else if("3".equals(importType)) {
					ser = csvXlsImport(fileInfo, conuid);
				}

				if(ser.getResultCode() != null) {
					fileImportResult.setResultCode(ser.getResultCode());
				}
				fileImportResult.setResultCount(ser.getResult());
				fileImportResult.setMessage(ser.getMessage());

			} catch (IOException | SQLException e) {
				fileImportResult.setResultCode(VarsqlAppCode.ERROR);
				fileImportResult.setMessage(e.getMessage());
				logger.error("file import error importType : {} , conuid : {} , fileInfo :{} " ,importType, fileInfo);
				logger.error("error message : {}" ,e.getMessage(), e);
			}

			resultInfo.add(fileImportResult);
		});

		result.setItemList(resultInfo);

		return result;
	}


	private SQLExecuteResult sqlImport(FileInfoEntity fileInfo, String conuid) throws IOException, SQLException {
		SqlStatementInfo ssi = new SqlStatementInfo();
		ssi.setConuid(conuid);

		UpdateExecutor baseExecutor = new UpdateExecutor();

		ssi.setSqlParam("{}");

		String insertQuery = FileUtils.readFileToString(FileServiceUtils.getUploadFile(fileInfo));
		ssi.setSql(insertQuery);

		SQLExecuteResult ser =baseExecutor.execute(ssi, new AbstractSQLExecutorHandler() {
			@Override
			public boolean handle(SQLHandlerParameter handleParam) {
				return true;
			}
		});

		return ser;
	}


	private SQLExecuteResult csvXlsImport(FileInfoEntity fileInfo, String conuid) {
		// TODO Auto-generated method stub
		return null;
	}


	private SQLExecuteResult jsonXmlImport(FileInfoEntity fileInfo, String conuid) {
		// TODO Auto-generated method stub
		return null;
	}





}