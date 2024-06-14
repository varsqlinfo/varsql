package com.varsql.web.app.database.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.varsql.web.app.database.service.DatabaseSourceGenImpl;
import com.varsql.web.app.database.service.PreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.UserCommonService;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DatabaseUtilsController.java
* @DESC		: database util controller.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/utils/gen")
public class DatabaseSourceGenerateController extends AbstractController  {

	private final Logger logger = LoggerFactory.getLogger(DatabaseSourceGenerateController.class);
	
	@Autowired
	private DatabaseSourceGenImpl databaseSourceGenImpl;
	
	/**
	 * 테이블 생성 DDL을 타 DB DDL 로 변경. 
	 * @param preferencesInfo
	 * @param schema
	 * @param databaseName
	 * @param convertDb
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/convertTableDDL", method = RequestMethod.POST)
	public @ResponseBody ResponseResult convertTableDDL(PreferencesRequestDTO preferencesInfo, 
			@RequestParam(value = "schema", required = true) String schema,
			@RequestParam(value = "convertDb", required = true) String convertDb,
			HttpServletRequest req,  HttpServletResponse res) throws Exception {
		
		return VarsqlUtils.getResponseResultItemList(databaseSourceGenImpl.convertTableDDL(preferencesInfo, schema, convertDb));
	}
	
	/**
	 * 테이블 정보 생성.
	 * @param preferencesInfo
	 * @param schema
	 * @param databaseName
	 * @param convertDb
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/createTableDDL", method = RequestMethod.POST)
	public @ResponseBody ResponseResult createTableDDL(PreferencesRequestDTO preferencesInfo, 
			@RequestParam(value = "schema", required = true) String schema,
			@RequestParam(value = "convertDb", required = true) String convertDb,
			@RequestParam(value = "templateParam", required = true) String templateParam,
			
			HttpServletRequest req,  HttpServletResponse res) throws Exception {
		
		return VarsqlUtils.getResponseResultItemList(databaseSourceGenImpl.createTableDDL(preferencesInfo, schema, convertDb, templateParam));
	}
}
