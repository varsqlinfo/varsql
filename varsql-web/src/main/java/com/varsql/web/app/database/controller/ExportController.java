package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.app.database.service.ExportServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
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

	private final Logger logger = LoggerFactory.getLogger(ExportController.class);

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
	@RequestMapping(value="/specMain" , method = RequestMethod.GET)
	public ModelAndView specMain(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {

		ModelMap model = mav.getModelMap();

		logger.debug("export specMain : {} ", preferencesInfo);

		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_EXPORT.key());
		exportServiceImpl.selectExportConfigInfo(preferencesInfo, model);

		return getModelAndView("/export/specMain", VIEW_PAGE.DATABASE_TOOLS, model);
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
	@RequestMapping(value="/specMain/tableList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult tableList(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {

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
	@RequestMapping(value="/spec/tableExport", method = RequestMethod.POST)
	public void tableExport(PreferencesRequestDTO preferencesInfo, HttpServletRequest req,  HttpServletResponse res) throws Exception {
		exportServiceImpl.tableSpecExport(preferencesInfo, req, res);
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
	@RequestMapping(value="/ddlMain", method =  RequestMethod.GET)
	public ModelAndView ddlMain(DatabaseParamInfo databaseParamInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();

		model.put("exportServiceMenu", MetaControlFactory.getDbInstanceFactory(databaseParamInfo.getDbType()).getServiceMenu());

		return getModelAndView("/export/ddlMain", VIEW_PAGE.DATABASE_TOOLS, model);
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
	@RequestMapping(value="/ddl/objInfo", method = RequestMethod.POST)
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
	@RequestMapping(value="/ddl/export", method = RequestMethod.POST)
	public void ddlExport(PreferencesRequestDTO preferencesInfo, HttpServletRequest req, HttpServletResponse res) throws Exception {
		exportServiceImpl.ddlExport(preferencesInfo, req, res);
	}
	
	/**
	 * @method  : tableDataExport
	 * @desc : table data export
	 * @author   : ytkim
	 * @date   : 2022. 1. 8. 
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/tableDataExport" }, method = { RequestMethod.GET })
	public ModelAndView tableDataExport(PreferencesRequestDTO preferencesInfo, ModelAndView mav, HttpServletRequest req)
			throws Exception {
		ModelMap model = mav.getModelMap();

		logger.debug("table data export : {} ", preferencesInfo);

		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_DATA_EXPORT.key());
		exportServiceImpl.selectExportConfigInfo(preferencesInfo, model);

		return getModelAndView("/export/tableDataExport", VIEW_PAGE.DATABASE_TOOLS, model);
	}
	  
	@PostMapping(value = { "/downloadTableData" })
	public void downloadTableData(PreferencesRequestDTO preferencesInfo, HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		this.logger.debug("downloadTableData data export : {} ", preferencesInfo);

		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_DATA_EXPORT.key());

		exportServiceImpl.downloadTableData(preferencesInfo, req, response);
	}

	@PostMapping(value = { "/downloadTableData2" })
	@ResponseBody
	public ResponseResult downloadTableData2(PreferencesRequestDTO preferencesInfo, HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		this.logger.debug("downloadTableData data export : {} ", preferencesInfo);

		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.TABLE_DATA_EXPORT.key());

		return exportServiceImpl.downloadTableData2(preferencesInfo, req, response);
	}
}
