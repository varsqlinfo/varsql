package com.varsql.app.database.service;
import java.util.Arrays;
import java.util.HashMap;
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
import com.varsql.app.database.dao.PreferencesDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.constants.VarsqlConstants;
import com.varsql.db.report.VarsqlReportConfig;
import com.varsql.db.util.DbInstanceFactory;
import com.vartech.common.app.beans.EnumMapperValue;
import com.vartech.common.excel.ExcelReport;
import com.vartech.common.utils.VartechUtils;

/**
 * 
 * @FileName  : AdminServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class ExportServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);
	
	@Autowired
	private ExportDAO exportDAO;
	
	@Autowired
	private PreferencesDAO preferencesDAO ;
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
	public void selectTableExportConfigInfo(PreferencesInfo preferencesInfo, ModelMap model) throws Exception {
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());
		DbInstanceFactory dbMetaEnum= VarsqlUtil.getConnidToDbInstanceFactory(preferencesInfo.getConuid());
		
		model.addAttribute("tableInfo",dbMetaEnum.getDBMeta().getTables(preferencesInfo));
		model.addAttribute("columnInfo",Arrays.stream(VarsqlReportConfig.TABLE.values()).map(EnumMapperValue::new).collect(Collectors.toList()));
		model.addAttribute("userSettingInfo",preferencesDAO.selectPreferencesInfo(preferencesInfo ,true));
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
	public void tableExport(PreferencesInfo preferencesInfo, HttpServletResponse res) throws Exception {
		
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());
		
		String jsonString = preferencesInfo.getPrefVal();
		
		logger.info("databaseParamInfo :{}", VartechUtils.reflectionToString(preferencesInfo));
		logger.info("settingInfo :{}", jsonString );
		
		if(preferencesDAO.selectPreferencesInfo(preferencesInfo)==null){
			preferencesDAO.insertPreferencesInfo(preferencesInfo);
		}else{
			preferencesDAO.updatePreferencesInfo(preferencesInfo);
		}
		
		DataCommonVO settingInfo = VartechUtils.stringToObject(jsonString, DataCommonVO.class);
		
		List<Map> tables = (List<Map>)settingInfo.get("tables");
		List<Map> columns = (List<Map>)settingInfo.get("columns");
		
		String[] tableNmArr =  Arrays.stream(tables.toArray(new HashMap[tables.size()])).map(tmp -> tmp.get("TABLE_NAME")).toArray(String[]::new);
		
		ExcelReport excelReport=VarsqlUtil.getDbInstanceFactory(preferencesInfo.getDbType()).getTableReportImpl().columnsInfo(preferencesInfo, columns, settingInfo.getBoolean("sheetFlag"), tableNmArr);
		
		String exportFileName =settingInfo.getString("exportName","table-spec");
		
		exportFileName += exportFileName.endsWith(".xlsx") ?"" : ".xlsx";
		
		VarsqlUtil.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName,VarsqlConstants.CHAR_SET));
		
		excelReport.write(res.getOutputStream());
	}
}