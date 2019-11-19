package com.varsql.app.database.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.constants.PreferencesConstants;
import com.varsql.app.common.enums.VIEW_PAGE;
import com.varsql.app.common.web.AbstractController;
import com.varsql.app.database.beans.PreferencesInfo;
import com.varsql.app.database.service.ExportServiceImpl;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ExportController.java
* @DESC		: export 처리. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/tools/export")
public class ExportController extends AbstractController  {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExportController.class);
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	
	/**
	 * 
	 * @Method Name  : specMain
	 * @Method 설명 : 명세서
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24. 
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/specMain")
	public ModelAndView specMain(PreferencesInfo preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		
		ModelMap model = mav.getModelMap();
		
		logger.debug("export specMain : {} ", preferencesInfo);
		
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());
		exportServiceImpl.selectExportConfigInfo(preferencesInfo, model);
		
		return getModelAndView("/exportMain/spec/specMain", VIEW_PAGE.DATABASE_TOOLS, model);
	}
	
	/**
	 * 
	 * @Method Name  : specMainTableInfoList
	 * @Method 설명 : table list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 29. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/specMain/tableList")
	public @ResponseBody ResponseResult tableList(PreferencesInfo preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		
		logger.debug("export specMain tableInfoList : {} ", preferencesInfo);
		
		return exportServiceImpl.selectExportTableInfo(preferencesInfo);
	}
	
	
	/**
	 * 
	 * @Method Name  : tableExport
	 * @Method 설명 : 테이블 명세서 다운로드.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/spec/tableExport")
	public void tableExport(PreferencesInfo preferencesInfo, HttpServletRequest req,  HttpServletResponse res) throws Exception {
		exportServiceImpl.tableSpecExport(preferencesInfo, res);
	}
	
	/**
	 * 
	 * @Method Name  : ddlMain
	 * @Method 설명 : ddl 메인
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24. 
	 * @변경이력  :
	 * @param databaseParamInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ddlMain")
	public ModelAndView ddlMain(DatabaseParamInfo databaseParamInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		model.put("exportServiceMenu", MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid()).getServiceMenu());
		
		return getModelAndView("/exportMain/ddl/ddlMain", VIEW_PAGE.DATABASE_TOOLS, model);
	}
	
	/**
	 * 
	 * @Method Name  : ddl db object info
	 * @Method 설명 : table ddl 화면보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ddl/objInfo")
	public @ResponseBody ResponseResult objInfo(DatabaseParamInfo paramInfo, HttpServletRequest req) throws Exception {
		paramInfo.setCustom(HttpUtils.getServletRequestParam(req));
		return  exportServiceImpl.selectExportDbObjectInfo(paramInfo);
	}

	
	
	/**
	 * 
	 * @Method Name  : ddlTableExport
	 * @Method 설명 : ddl table export
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ddl/export")
	public void ddlExport(PreferencesInfo preferencesInfo, HttpServletResponse res) throws Exception {
		exportServiceImpl.ddlExport(preferencesInfo, res);
	}

}
