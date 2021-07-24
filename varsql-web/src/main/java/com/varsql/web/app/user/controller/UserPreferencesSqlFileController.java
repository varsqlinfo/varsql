package com.varsql.web.app.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.user.service.UserPreferencesSqlFileServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VarsqlParamConstants;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

/**
 *
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserPreferencesSqlFileController.java
* @DESC		: sql file 컨트롤러.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 28. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/user/preferences/sqlFile")
public class UserPreferencesSqlFileController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(UserPreferencesSqlFileController.class);

	@Autowired
	private UserPreferencesSqlFileServiceImpl userPreferencesSqlFileServiceImpl;

	/**
	 *
	 * @Method Name  : list
	 * @Method 설명 : 목록 .
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public @ResponseBody ResponseResult list(@RequestParam(value = "vconnid", required = true)  String vconnid, HttpServletRequest req) {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.userViewId(req));

		return  userPreferencesSqlFileServiceImpl.sqlFileList(vconnid, searchParameter);
	}

	/**
	 *
	 * @Method Name  : detail
	 * @Method 설명 : 상세
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/detail",method = RequestMethod.POST)
	public @ResponseBody ResponseResult detail(@RequestParam(value = "sqlId", required = true)  String sqlId) {
		return  userPreferencesSqlFileServiceImpl.selectSqlFileDetail(sqlId);
	}

	/**
	 *
	 * @Method Name  : delete
	 * @Method 설명 : 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 11. 1.
	 * @변경이력  :
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/delete" ,method = RequestMethod.POST)
	public @ResponseBody ResponseResult delete(@RequestParam(value = "selectItem", required = true )  String selectItem , HttpServletRequest req){
		logger.debug("remove sql file removeIds : {}" , selectItem);
		return  userPreferencesSqlFileServiceImpl.deleteSqlFiles(selectItem);
	}
}
