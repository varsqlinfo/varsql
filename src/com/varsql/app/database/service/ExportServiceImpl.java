package com.varsql.app.database.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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
import com.varsql.core.common.constants.BlankConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.varsql.core.db.beans.ddl.DDLCreateOption;
import com.varsql.core.db.beans.ddl.DDLInfo;
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
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		model.addAttribute("userSettingInfo",preferencesServiceImpl.selectPreferencesInfo(preferencesInfo ,true));
		model.addAttribute("columnInfo",Arrays.stream(VarsqlReportConfig.TABLE_COLUMN.values()).map(EnumMapperValue::new).collect(Collectors.toList()));
		
		if(SecurityUtil.isSchemaView(preferencesInfo)) {
			model.addAttribute("schemaList",dbMetaEnum.getSchemas(preferencesInfo));
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
	public ResponseResult selectExportTableInfo(PreferencesInfo preferencesInfo) throws Exception {
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		ResponseResult result =new ResponseResult();
		
		result.setItemList(dbMetaEnum.getDBObjectList(DBObjectType.TABLE.getObjectTypeId(),preferencesInfo));
		
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
	public ResponseResult selectExportDbObjectInfo(DatabaseParamInfo databaseParam) throws Exception {
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParam.getConuid());
		
		Map customParam = databaseParam.getCustom(); 
		
		String ddlObjInfo = String.valueOf(customParam.get("ddlObjInfo"));
		
		String[] objArr =ddlObjInfo.split(",");
		
		ResponseResult result = new ResponseResult();
		
		for (int i = 0; i < objArr.length; i++) {
			String mode = objArr[i];
			result.addCustoms(mode, dbMetaEnum.getDBObjectList(DBObjectType.getDBObjectType(mode).getObjectTypeId() , databaseParam));
		}
		
		return result;
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
		
		ExcelReport excelReport=MetaControlFactory.getDbInstanceFactory(preferencesInfo.getDbType()).getTableReportImpl().columnsInfo(preferencesInfo, columns,settingInfo.getBoolean("addTableDefinitionFlag", false) ,settingInfo.getBoolean("sheetFlag" ,false), tableNmArr);
		
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
	public void ddlExport(PreferencesInfo preferencesInfo, HttpServletResponse res) throws Exception {
		String jsonString = preferencesInfo.getPrefVal();
		
		logger.debug("ddlExport PreferencesInfo :{}", VartechUtils.reflectionToString(preferencesInfo));
		logger.debug("settingInfo :{}", jsonString );
		
		DataCommonVO settingInfo = VartechUtils.stringToObject(jsonString, DataCommonVO.class);
		
		Map<String, List<Map>> exportInfo = (Map<String, List<Map>>)settingInfo.get("exportInfo");
		
		Iterator<String> iter =exportInfo.keySet().iterator();
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		StringBuilder allDDLScript = new StringBuilder();
		DDLCreateOption ddlOption = new DDLCreateOption();
		while(iter.hasNext()){
			String objectName = iter.next();
			
			allDDLScript.append("--------- "+objectName+" start----------").append(BlankConstants.NEW_LINE_TWO);
			List<Map> objList =  exportInfo.get(objectName);
			String[] objNmArr =  Arrays.stream(objList.toArray(new HashMap[objList.size()])).map(tmp -> tmp.get("name")).toArray(String[]::new);
			
			preferencesInfo.setObjectType(DBObjectType.getDBObjectType( objectName).name());
			
			List<DDLInfo> ddlList = dbMetaEnum.getDDLScript(DBObjectType.getDBObjectType( objectName).getObjectTypeId(),preferencesInfo,ddlOption, objNmArr);
			
			for (DDLInfo ddlInfo : ddlList) {
				allDDLScript.append(ddlInfo.getCreateScript()).append(BlankConstants.NEW_LINE);
			}
			
			allDDLScript.append(BlankConstants.NEW_LINE).append("--------- // "+objectName+" end----------").append(BlankConstants.NEW_LINE_THREE);
			
		}
		
		String exportFileName =settingInfo.getString("exportName","export-ddl");
		
		exportFileName += exportFileName.endsWith(".sql") ?"" : ".sql";
		
		VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName,VarsqlConstants.CHAR_SET));
		
		VarsqlUtil.textDownload(res.getOutputStream(), allDDLScript.toString());
		
	}
}