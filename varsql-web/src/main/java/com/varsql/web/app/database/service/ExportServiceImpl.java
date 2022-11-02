package com.varsql.web.app.database.service;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.varsql.core.common.beans.ProgressInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.code.VarsqlFileType;
import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.data.writer.SQLWriter;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.report.VarsqlReportConfig;
import com.varsql.core.db.servicemenu.ObjectType;
import com.varsql.core.db.valueobject.BaseObjectInfo;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.ddl.DDLCreateOption;
import com.varsql.core.db.valueobject.ddl.DDLInfo;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.executor.SQLExecuteResult;
import com.varsql.core.sql.executor.SelectExecutor;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.web.constants.HttpSessionConstants;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.DDLExportItemVO;
import com.varsql.web.dto.DDLExportVO;
import com.varsql.web.dto.DataExportItemVO;
import com.varsql.web.dto.DataExportVO;
import com.varsql.web.dto.db.DBMetadataRequestDTO;
import com.varsql.web.dto.sql.SqlExecuteDTO;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.exception.DataDownloadException;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.app.FileInfoEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.ValidateUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.EnumMapperValue;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.excel.ExcelReport;
import com.vartech.common.io.writer.AbstractWriter;
import com.vartech.common.io.writer.CSVWriter;
import com.vartech.common.io.writer.ExcelWriter;
import com.vartech.common.io.writer.JSONWriter;
import com.vartech.common.io.writer.WriteMetadataInfo;
import com.vartech.common.io.writer.XMLWriter;
import com.vartech.common.utils.FileUtils;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.IOUtils;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechUtils;

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
public class ExportServiceImpl{
	private final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

	@Autowired
	private PreferencesServiceImpl preferencesServiceImpl;
	
	@Autowired
	private FileInfoEntityRepository fileInfoEntityRepository;
	
	private int ZIP_BUFFER_SIZE = 2048;

	/**
	 *
	 * @Method Name  : selectConfigInfo
	 * @Method 설명 : table export Info
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 22.
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param model
	 * @throws Exception
	 */
	public void selectExportConfigInfo(PreferencesRequestDTO preferencesInfo, ModelMap model) throws Exception {
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));
			
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());

		model.addAttribute("userSettingInfo",preferencesServiceImpl.selectPreferencesInfo(preferencesInfo ,true));
		model.addAttribute("columnInfo",Arrays.stream(VarsqlReportConfig.TABLE_COLUMN.values()).map(EnumMapperValue::new).collect(Collectors.toList()));

		if(SecurityUtil.isSchemaView(dpi)) {
			
			DBVenderType venderType = DBVenderType.getDBType(dpi.getType());

			if(venderType.isUseDatabaseName()) {
				model.addAttribute("schemaList", dbMetaEnum.getDatabases(dpi));
			}else {
				model.addAttribute("schemaList", dbMetaEnum.getSchemas(dpi));
			}
		}else {
			model.addAttribute("schemaInfo", "");
		}
	}

	/**
	 *
	 * @Method Name  : selectExportTableInfo
	 * @Method 설명 : table export list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 29.
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseResult selectExportTableInfo(PreferencesRequestDTO preferencesInfo) throws Exception {
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());

		ResponseResult result =new ResponseResult();

		result.setList(dbMetaEnum.getDBObjectList(ObjectType.TABLE.getObjectTypeId(), dpi));

		return result ;

	}

	/**
	 *
	 * @Method Name  : selectExportDbObjectInfo
	 * @Method 설명 : db object info
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 29.
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param mode
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public ResponseResult selectExportDbObjectInfo(DBMetadataRequestDTO dbMetadataRequestDTO) throws Exception {
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dbMetadataRequestDTO.getDbType());

		Map customParam = dbMetadataRequestDTO.getCustom();

		String ddlObjInfo = String.valueOf(customParam.get("ddlObjInfo"));

		String[] objArr =ddlObjInfo.split(",");

		ResponseResult result = new ResponseResult();

		for (int i = 0; i < objArr.length; i++) {
			String mode = objArr[i];
			result.addCustomMapAttribute(mode, dbMetaEnum.getDBObjectList(ObjectType.getDBObjectType(mode).getObjectTypeId(), dbMetadataRequestDTO));
		}

		return result;
	}


	/**
	 * 테이블 정보 내보내기.
	 *
	 * @method : tableSpecExport
	 * @param preferencesInfo
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void tableSpecExport(PreferencesRequestDTO preferencesInfo, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));

		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());

		String jsonString = preferencesInfo.getPrefVal();

		logger.debug("tableSpecExport :{}", VartechUtils.reflectionToString(preferencesInfo));
		logger.debug("settingInfo :{}", jsonString );
		logger.debug("MetaControlFactory.getDbInstanceFactory(preferencesInfo.getDbType()).getTableReportImpl() :{}", MetaControlFactory.getDbInstanceFactory(dpi.getDbType()).getTableReportImpl() );

		preferencesServiceImpl.savePreferencesInfo(preferencesInfo); // 설정 정보 저장.

		DataMap settingInfo = VartechUtils.jsonStringToObject(jsonString, DataMap.class);

		List<Map> tables = (List<Map>)settingInfo.get("tables");
		List<Map> columns = (List<Map>)settingInfo.get("columns");

		String[] tableNmArr =  Arrays.stream(tables.toArray(new HashMap[tables.size()])).map(tmp -> tmp.get("name")).toArray(String[]::new);

		ExcelReport excelReport=MetaControlFactory.getDbInstanceFactory(dpi.getDbType()).getTableReportImpl().columnsInfo(dpi, columns,settingInfo.getBoolean("addTableDefinitionFlag", false) ,settingInfo.getBoolean("sheetFlag" ,false), tableNmArr);

		String exportFileName =settingInfo.getString("exportName","table-spec");

		exportFileName += exportFileName.endsWith(".xlsx") ?"" : ".xlsx";

		VarsqlUtils.setResponseDownAttr(res, req, exportFileName);

		excelReport.write(res.getOutputStream());
	}

	/**
	 * @Method Name  : tableDDLExport
	 * @Method 설명 : 테이블 ddl 내보내기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 8. 24.
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param req 
	 * @param res
	 * @throws Exception
	 */
	public void ddlExport(PreferencesRequestDTO preferencesInfo, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String jsonString = preferencesInfo.getPrefVal();
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(preferencesInfo.getConuid()));

		logger.debug("ddlExport PreferencesInfo :{}", VartechUtils.reflectionToString(preferencesInfo));
		logger.debug("settingInfo :{}", jsonString );

		DataMap settingInfo = VartechUtils.jsonStringToObject(jsonString, DataMap.class);

		Map<String, List<Map>> exportInfo = (Map<String, List<Map>>)settingInfo.get("exportInfo");

		Iterator<String> iter =exportInfo.keySet().iterator();

		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(dpi.getDbType());

		StringBuilder allDDLScript = new StringBuilder();
		DDLCreateOption ddlOption = new DDLCreateOption();
		while(iter.hasNext()){
			String objectName = iter.next();

			allDDLScript.append("--------- "+objectName+" start----------").append(BlankConstants.NEW_LINE_TWO);
			List<Map> objList =  exportInfo.get(objectName);
			String[] objNmArr =  Arrays.stream(objList.toArray(new HashMap[objList.size()])).map(tmp -> tmp.get("name")).toArray(String[]::new);

			ObjectType objectType = ObjectType.getDBObjectType( objectName);
			
			dpi.setObjectType(objectType.name());

			List<DDLInfo> ddlList = dbMetaEnum.getDDLScript(objectType.getObjectTypeId(), dpi, ddlOption, objNmArr);

			for (DDLInfo ddlInfo : ddlList) {
				allDDLScript.append(ddlInfo.getCreateScript()).append(BlankConstants.NEW_LINE_TWO);
			}

			allDDLScript.append(BlankConstants.NEW_LINE).append("--------- // "+objectName+" end----------").append(BlankConstants.NEW_LINE_THREE);

		}

		String exportFileName =settingInfo.getString("exportName","export-ddl");

		exportFileName += exportFileName.endsWith(".sql") ?"" : ".sql";

		VarsqlUtils.setResponseDownAttr(res, req, exportFileName);

		VarsqlUtils.textDownload(res.getOutputStream(), allDDLScript.toString());

	}
	
	/**
	 * download table data 
	 *
	 * @method : downloadTableData
	 * @param preferencesInfo
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void downloadTableData(PreferencesRequestDTO preferencesInfo, HttpServletRequest req, HttpServletResponse res) {
		
		
		String requid = HttpUtils.getString(req, "requid");
		String sessAttrKey = HttpSessionConstants.progressKey(requid);
		
		HttpSession session = req.getSession();
		
		try {
			logger.debug("downloadTableData : {}", preferencesInfo.getPrefVal());
			
			DataExportVO dataExportVO = VartechUtils.jsonStringToObject(preferencesInfo.getPrefVal(), DataExportVO.class, true);
	
			ProgressInfo progressInfo = new ProgressInfo();
	
			progressInfo.setTotalItemSize(dataExportVO.getExportItems().size());
			
			session.setAttribute(sessAttrKey, progressInfo);
			
			
			Path fileExportPath = FileServiceUtils.getSavePath(UploadFileType.EXPORT);
			File zipFile = new File(FileUtils.pathConcat(fileExportPath.toAbsolutePath().toString(),
					VarsqlFileType.ZIP.concatExtension(ValidateUtils.getValidFileName(dataExportVO.getFileName()))));
			
			FileInfoEntity fie = exportTableData(SecurityUtil.loginInfo().getDatabaseInfo().get(dataExportVO.getConuid()), dataExportVO, zipFile, progressInfo);
	
			fileInfoEntityRepository.save(fie);
	
			session.setAttribute(sessAttrKey, "complete");
	
			VarsqlUtils.setResponseDownAttr(res, req, fie.getFileName());
	
			res.setHeader("Content-Length", "" + fie.getFileSize());
	
			File file = FileServiceUtils.getFileInfoToFile(fie);
			byte[] b = new byte[2048];
			try (BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
					BufferedOutputStream outs = new BufferedOutputStream(res.getOutputStream())) {
				int read = 0;
				while ((read = fin.read(b)) != -1) {
					outs.write(b, 0, read);
				}
	
				outs.flush();
	
				IOUtils.close(fin);
				IOUtils.close(outs);
			}
		}catch(Exception e) {
			session.setAttribute(sessAttrKey, "fail");
			throw new DataDownloadException(VarsqlAppCode.COMM_FILE_DOWNLOAD_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * db table data export
	 *
	 * @method : exportTableData
	 * @param databaseInfo
	 * @param dataExportVO
	 * @param zipFile
	 * @return
	 * @throws Exception
	 */
	public FileInfoEntity exportTableData(DatabaseInfo databaseInfo, DataExportVO dataExportVO, File zipFile) throws Exception {
		return exportTableData(databaseInfo, dataExportVO, zipFile, null);
	}
	
	/**
	 * db table data export
	 *
	 * @method : exportTableData
	 * @param databaseInfo
	 * @param dataExportVO
	 * @param zipFile
	 * @param progressInfo
	 * @return
	 * @throws Exception
	 */
	public FileInfoEntity exportTableData(DatabaseInfo databaseInfo, DataExportVO dataExportVO, File zipFile, ProgressInfo progressInfo) throws Exception {
		
		String prefixSchema = StringUtils.isBlank(dataExportVO.getSchema()) ? "" : dataExportVO.getSchema() + ".";
		String charset = StringUtils.isBlank(dataExportVO.getCharset()) ? VarsqlConstants.CHAR_SET : dataExportVO.getCharset();

		String exportFileName = zipFile.getName();

		final VarsqlFileType exportType = dataExportVO.getExportType();
		
		DBVenderType dbType = DBVenderType.getDBType(databaseInfo.getType());

		String exportTempFilePath = FileServiceUtils.getSavePath(UploadFileType.TEMP).toAbsolutePath().toString();
		
		Map<String, SQLExecuteResult> tableExportCount = new HashMap<>();
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile), Charset.forName(charset));) {

			int idx = 0;
			for (DataExportItemVO item : dataExportVO.getExportItems()) {
				++idx;

				SqlExecuteDTO seDto = new SqlExecuteDTO();
				seDto.setLimit(dataExportVO.getLimit());
				seDto.setDatabaseInfo(databaseInfo);

				String objectName = prefixSchema + item.getName();

				seDto.setSqlParam("{}");
				seDto.setSql(SQLUtils.generateSelectQuery(objectName, item.getCondition(), dbType));

				String exportFilePath = FileUtils.pathConcat(exportTempFilePath, exportType.concatExtension(VartechUtils.generateUUID()));

				if(progressInfo != null) {
					progressInfo.setName(item.getName());
					progressInfo.setItemIdx(idx);
				}

				AbstractWriter writer = null;
				try (OutputStream outstream = new FileOutputStream(exportFilePath);) {

					if (VarsqlFileType.CSV.equals(exportType)) {
						writer = new CSVWriter(outstream, ',', charset);
					} else if (VarsqlFileType.JSON.equals(exportType)) {
						writer = new JSONWriter(outstream, "row", charset);
					} else if (VarsqlFileType.XML.equals(exportType)) {
						writer = new XMLWriter(outstream, "row", charset);
					} else if (VarsqlFileType.EXCEL.equals(exportType)) {
						writer = new ExcelWriter(outstream);
					} else {
						writer = new SQLWriter(outstream, dbType, objectName, charset);
					}

					final String tableName = item.getName();
					SQLExecuteResult ser = (new SelectExecutor()).execute(seDto,new SelectExecutorHandler(writer) {
						private boolean firstFlag = true;
						private int rowIdx = 0;

						public boolean handle(SelectInfo handleParam) {
							if (this.firstFlag) {
								WriteMetadataInfo whi = new WriteMetadataInfo("exportInfo");
								List<ExportColumnInfo> columns = new LinkedList<>();
								handleParam.getColumnInfoList().forEach(item -> {
									ExportColumnInfo gci = new ExportColumnInfo();
									gci.setName(item.getLabel());

									// 추가 할것.
									gci.setAlias(item.getKey());
									gci.setType(item.getDbType());
									gci.setNumber(item.isNumber());
									gci.setLob(item.isLob());
									columns.add(gci);
								});
								whi.addMetedata("tableName", tableName);
								whi.addMetedata("columns", columns);
								getWriter().setMetadata(new WriteMetadataInfo[] { whi });
								if (VarsqlFileType.SQL.equals(exportType)) {
									((SQLWriter) getWriter()).setColumnInfos(handleParam.getColumnInfoList());
								}
								this.firstFlag = false;
							} 

							if(progressInfo != null) {
								progressInfo.setProgressContentLength(++rowIdx);
							}

							try {
								getWriter().addRow(handleParam.getRowObject());
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
								return false;
							}
							return true;
						}
					});
					
					logger.debug("data export result :{} ", ser);
					
					tableExportCount.put(objectName, ser);
					
					writer.writeAndClose();

					String zipFileName = exportType.concatExtension(item.getName());

					if (ser.getResultCode() != null) {
						VarsqlUtils.textDownload(new FileOutputStream(exportFilePath), ser.getMessage());
						zipFileName = item.getName() + "-export-error.txt";
					}

					try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(exportFilePath));) {
						ZipEntry zentry = new ZipEntry(zipFileName);
						zentry.setSize(new File(exportFilePath).length());
						zos.putNextEntry(zentry);

						byte[] buffer = new byte[ZIP_BUFFER_SIZE];
						int cnt = 0;
						while ((cnt = bis.read(buffer, 0, ZIP_BUFFER_SIZE)) != -1) {
							zos.write(buffer, 0, cnt);
						}
						zos.closeEntry();
					}

					new File(exportFilePath).delete();

				} catch (Exception e) {
					throw e;
				} finally {
					IOUtils.close(writer);
				}
			}
			IOUtils.close(zos);
		}
		
		String fileId = VartechUtils.generateUUID();
		
		FileInfoEntity fie = FileInfoEntity.builder().fileId(fileId)
				.fileContId(fileId)
				.fileDiv(UploadFileType.EXPORT.getDiv())
				.contGroupId(databaseInfo.getVconnid())
				.fileFieldName("exportZipFile")
				.fileName(exportFileName)
				.fileSize(zipFile.length())
				.fileExt(VarsqlFileType.ZIP.getExtension())
				.filePath(FileUtils.pathConcat(FileServiceUtils.getSaveRelativePath(UploadFileType.EXPORT), zipFile.getName()))
				.customInfo(tableExportCount)
				.build();
		
		return fie; 
	}
	
	/**
	 * export ddl
	 *
	 * @method : exportDDLInfo
	 * @param databaseInfo
	 * @param ddlExportVO
	 * @param zipFile
	 * @param progressInfo
	 * @return
	 * @throws Exception
	 */
	public FileInfoEntity exportDDLInfo(DatabaseInfo databaseInfo, DDLExportVO ddlExportVO, File zipFile) throws Exception {
		return exportDDLInfo(databaseInfo, ddlExportVO, zipFile, null);
	}
	public FileInfoEntity exportDDLInfo(DatabaseInfo databaseInfo, DDLExportVO ddlExportVO, File zipFile, ProgressInfo progressInfo) throws Exception {
		
		String charset = StringUtils.isBlank(ddlExportVO.getCharset()) ? VarsqlConstants.CHAR_SET : ddlExportVO.getCharset();
	
		String exportFileName = zipFile.getName();

		String exportTempFilePath = FileServiceUtils.getSavePath(UploadFileType.TEMP).toAbsolutePath().toString();
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getDbInstanceFactory(databaseInfo.getType());
		DDLCreateOption ddlOption = new DDLCreateOption();
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(databaseInfo);
		
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile), Charset.forName(charset));) {
			
			for(DDLExportItemVO exportItemVO : ddlExportVO.getExportItems()) {
				ObjectType objectType = ObjectType.getDBObjectType(exportItemVO.getContentid());
				
				dpi.setObjectType(objectType.name());
				
				String[] objNmArr = null;
				if(exportItemVO.isAllObjectExport()) {
					List<BaseObjectInfo> objectList = dbMetaEnum.getDBObjectList(objectType.getObjectTypeId(), dpi);
					objNmArr = objectList.stream().map(tmp->tmp.getName()).collect(Collectors.toList()).toArray(new String[0]);
				}else {
					objNmArr = exportItemVO.getObjectNameList().toArray(new String[0]);
				}
				
				List<DDLInfo> ddlList = dbMetaEnum.getDDLScript(objectType.getObjectTypeId(), dpi, ddlOption, objNmArr);

				for (DDLInfo ddlInfo : ddlList) {
					String exportFilePath = FileUtils.pathConcat(exportTempFilePath, VarsqlFileType.SQL.concatExtension(VartechUtils.generateUUID()));
					
					VarsqlUtils.textDownload(new FileOutputStream(exportFilePath), ddlInfo.getCreateScript());
					
					try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(exportFilePath));) {
						ZipEntry zentry = new ZipEntry(VarsqlFileType.SQL.concatExtension(ddlInfo.getName()));
						zentry.setSize(new File(exportFilePath).length());
						zos.putNextEntry(zentry);
						
						byte[] buffer = new byte[ZIP_BUFFER_SIZE];
						int cnt = 0;
						while ((cnt = bis.read(buffer, 0, ZIP_BUFFER_SIZE)) != -1) {
							zos.write(buffer, 0, cnt);
						}
						zos.closeEntry();
					}
					new File(exportFilePath).delete();
				}
			}
			IOUtils.close(zos);
		}
		
		String fileId = VartechUtils.generateUUID();
		
		FileInfoEntity fie = FileInfoEntity.builder().fileId(fileId)
				.fileContId(fileId)
				.fileDiv(UploadFileType.EXPORT.getDiv())
				.contGroupId(databaseInfo.getVconnid())
				.fileFieldName("exportDDLZipFile")
				.fileName(VarsqlFileType.ZIP.concatExtension(exportFileName))
				.fileSize(zipFile.length())
				.fileExt(VarsqlFileType.ZIP.getExtension())
				.filePath(FileUtils.pathConcat(FileServiceUtils.getSaveRelativePath(UploadFileType.EXPORT), zipFile.getName()))
				.build();
		
		return fie; 
	}
}