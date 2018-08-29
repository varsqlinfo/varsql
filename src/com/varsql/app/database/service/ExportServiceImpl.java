package com.varsql.app.database.service;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.PreferencesConstants;
import com.varsql.app.database.beans.PreferencesInfo;
import com.varsql.app.database.dao.ExportDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.report.VarsqlReportConfig;
import com.vartech.common.app.beans.EnumMapperValue;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.excel.ExcelReport;
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
	private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);
	
	@Autowired
	private ExportDAO exportDAO;
	
	@Autowired
	private PreferencesServiceImpl preferencesServiceImpl;
	
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
	public void selectExportConfigInfo(PreferencesInfo preferencesInfo, ModelMap model) throws Exception {
		model.addAttribute("userSettingInfo",preferencesServiceImpl.selectPreferencesInfo(preferencesInfo ,true));
	}
	
	public void selectExportTableInfo(PreferencesInfo preferencesInfo, ModelMap model, boolean tableColumnInfoFlag) throws Exception {
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		model.addAttribute("tableInfo",dbMetaEnum.getDBMeta().getTables(preferencesInfo));
		
		if(tableColumnInfoFlag){
			model.addAttribute("columnInfo",Arrays.stream(VarsqlReportConfig.TABLE.values()).map(EnumMapperValue::new).collect(Collectors.toList()));
		}
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
	public String selectExportDbObjectInfo(PreferencesInfo preferencesInfo, String mode, ModelMap model) throws Exception{
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		String viewPage =  mode;
		
		if(DBObjectType.TABLE.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getTables(preferencesInfo));
		}else if(DBObjectType.VIEW.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getViews(preferencesInfo));
		}else if(DBObjectType.PROCEDURE.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getProcedures(preferencesInfo));
		}else if(DBObjectType.FUNCTION.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getFunctions(preferencesInfo));
		}else if(DBObjectType.INDEX.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getIndexs(preferencesInfo));
		}else if(DBObjectType.TRIGGER.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getTriggers(preferencesInfo));
		}else if(DBObjectType.SEQUENCE.getObjName().equals(mode)){
			model.addAttribute("exportInfo", dbMetaEnum.getDBMeta().getSequences(preferencesInfo));
		}else{
			viewPage = "all";
		}
		
		return viewPage; 
	}
	
	
	/**
	 * 
	 * @param paramInfo 
	 * @Method Name  : tableExport
	 * @Method 설명 : 테이블 정보 내보내기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 23. 
	 * @변경이력  :
	 * @param databaseParamInfo 
	 * @param paramInfo
	 * @param res
	 * @throws Exception 
	 */
	public void tableSpecExport(PreferencesInfo preferencesInfo, HttpServletResponse res) throws Exception {
		
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());
		
		String jsonString = preferencesInfo.getPrefVal();
		
		logger.debug("tableSpecExport :{}", VartechUtils.reflectionToString(preferencesInfo));
		logger.debug("settingInfo :{}", jsonString );
		
		preferencesServiceImpl.savePreferencesInfo(preferencesInfo); // 설정 정보 저장.
		
		DataCommonVO settingInfo = VartechUtils.stringToObject(jsonString, DataCommonVO.class);
		
		List<Map> tables = (List<Map>)settingInfo.get("tables");
		List<Map> columns = (List<Map>)settingInfo.get("columns");
		
		String[] tableNmArr =  Arrays.stream(tables.toArray(new HashMap[tables.size()])).map(tmp -> tmp.get("name")).toArray(String[]::new);
		
		ExcelReport excelReport=MetaControlFactory.getDbInstanceFactory(preferencesInfo.getDbType()).getTableReportImpl().columnsInfo(preferencesInfo, columns, settingInfo.getBoolean("sheetFlag"), tableNmArr);
		
		String exportFileName =settingInfo.getString("exportName","table-spec");
		
		exportFileName += exportFileName.endsWith(".xlsx") ?"" : ".xlsx";
		
		VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName,VarsqlConstants.CHAR_SET));
		
		excelReport.write(res.getOutputStream());
	}
	
	/**
	 * @Method Name  : tableDDLExport
	 * @Method 설명 : 테이블 ddl 내보내기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 8. 24. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param res
	 * @throws Exception 
	 */
	public void tableDDLExport(PreferencesInfo preferencesInfo, HttpServletResponse res) throws Exception {
		String jsonString = preferencesInfo.getPrefVal();
		
		logger.debug("tableDDLExport PreferencesInfo :{}", VartechUtils.reflectionToString(preferencesInfo));
		logger.debug("settingInfo :{}", jsonString );
		
		DataCommonVO settingInfo = VartechUtils.stringToObject(jsonString, DataCommonVO.class);
		
		List<Map> tables = (List<Map>)settingInfo.get("tables");
		
		String[] tableNmArr =  Arrays.stream(tables.toArray(new HashMap[tables.size()])).map(tmp -> tmp.get("name")).toArray(String[]::new);
		
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		String ddlScript = dbMetaEnum.getDDLScript().getTable(preferencesInfo, tableNmArr);
		
		String exportFileName =settingInfo.getString("exportName","table-ddl");
		
		exportFileName += exportFileName.endsWith(".sql") ?"" : ".sql";
		
		VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName,VarsqlConstants.CHAR_SET));
		
		VarsqlUtil.textDownload(res.getOutputStream(), ddlScript);
		
	}
}