package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.database.service.ExportServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.db.DBMetadataRequestDTO;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class ExportController extends AbstractController  {

	private final Logger logger = LoggerFactory.getLogger(ExportController.class);

	private final ExportServiceImpl exportServiceImpl;

	/**
	 * 명세서
	 *
	 * @method : specMain
	 * @param preferencesInfo
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
	 * table list
	 *
	 * @method : tableList
	 * @param preferencesInfo
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/specMain/tableList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult tableList(DBMetadataRequestDTO dbMetadataRequestDTO, ModelAndView mav, HttpServletRequest req) throws Exception {

		logger.debug("export specMain tableInfoList : {} ", dbMetadataRequestDTO);
		

		return exportServiceImpl.selectExportTableInfo(dbMetadataRequestDTO);
	}

	/**
	 * 테이블 명세서 다운로드.
	 *
	 * @method : tableExport
	 * @param preferencesInfo
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value="/spec/tableExport", method = RequestMethod.POST)
	public void tableExport(PreferencesRequestDTO preferencesInfo, 
			@RequestParam(value = "schema", required = true) String schema,
			HttpServletRequest req,  HttpServletResponse res) throws Exception {
		exportServiceImpl.tableSpecExport(preferencesInfo, schema, req, res);
	}

	/**
	 * ddl 메인
	 *
	 * @method : ddlMain
	 * @param dbMetadataRequestDTO
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/ddlMain", method =  RequestMethod.GET)
	public ModelAndView ddlMain(DBMetadataRequestDTO dbMetadataRequestDTO, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		
		exportServiceImpl.ddlMainInfo(dbMetadataRequestDTO, model);

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
	public @ResponseBody ResponseResult objInfo(DBMetadataRequestDTO dbMetadataRequestDTO, HttpServletRequest req) throws Exception {
		dbMetadataRequestDTO.setCustom(HttpUtils.getServletRequestParam(req));
		return  exportServiceImpl.selectExportDbObjectInfo(dbMetadataRequestDTO);
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
	public void ddlExport(PreferencesRequestDTO preferencesInfo, @RequestParam(value = "schema", required = true) String schema, HttpServletRequest req, HttpServletResponse res) throws Exception {
		exportServiceImpl.ddlExport(preferencesInfo, schema, req, res);
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
	public @ResponseBody ResponseResult downloadTableData(PreferencesRequestDTO preferencesInfo, HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		this.logger.debug("downloadTableData data export : {} ", preferencesInfo);

		return exportServiceImpl.downloadTableData(preferencesInfo, req, response);
	}
}
