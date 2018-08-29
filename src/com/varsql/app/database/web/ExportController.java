package com.varsql.app.database.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.database.beans.PreferencesInfo;
import com.varsql.app.database.service.ExportServiceImpl;
import com.varsql.app.exception.DatabaseInvalidException;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.DBObjectType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.beans.DatabaseParamInfo;

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
public class ExportController {

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
	public ModelAndView specMain(DatabaseParamInfo databaseParamInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		return  new ModelAndView("/database/tools/exportMain/spec/specMain",model);
	}
	
	/**
	 * 
	 * @Method Name  : specTable
	 * @Method 설명 : 테이블 명세서 화면보기
	 * @작성자   : ytkim
	 * @작성일   : 2018. 8. 24. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/spec/table")
	public ModelAndView specTable(PreferencesInfo preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		exportServiceImpl.selectExportConfigInfo(preferencesInfo, model);
		exportServiceImpl.selectExportTableInfo(preferencesInfo, model, true);
		return  new ModelAndView("/database/tools/export/spec/tableSpec",model);
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
		
		MetaControlBean dbMetaEnum= MetaControlFactory.getConnidToDbInstanceFactory(databaseParamInfo.getConuid());
		model.put("exportServiceMenu", dbMetaEnum.getDBMeta().getServiceMenu());
		
		return  new ModelAndView("/database/tools/exportMain/ddl/ddlMain",model);
	}
	
	/**
	 * 
	 * @Method Name  : ddlTable
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
	@RequestMapping("/ddl/{mode}")
	public ModelAndView ddlTable(PreferencesInfo preferencesInfo,@PathVariable String mode, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		String viewPage = exportServiceImpl.selectExportDbObjectInfo(preferencesInfo, mode, model); 
		
		return  new ModelAndView("/database/tools/export/ddl/"+viewPage+"Ddl",model);
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
	@RequestMapping("/ddl/tableExport")
	public void ddlTableExport(PreferencesInfo preferencesInfo, HttpServletResponse res) throws Exception {
		exportServiceImpl.tableDDLExport(preferencesInfo, res);
	}

}
