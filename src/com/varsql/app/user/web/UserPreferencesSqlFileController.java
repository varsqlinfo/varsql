package com.varsql.app.user.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.common.web.AbstractController;
import com.varsql.app.user.service.UserPreferencesSqlFileServiceImpl;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ParamMap;
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

	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesSqlFileController.class);
	
	@Autowired
	UserPreferencesSqlFileServiceImpl userPreferencesSqlFileServiceImpl;
	
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
	@RequestMapping(value={"/list"},method = RequestMethod.POST)
	public @ResponseBody ResponseResult list(HttpServletRequest req) {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return  userPreferencesSqlFileServiceImpl.sqlFileList(searchParameter);
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
	@RequestMapping(value={"/detail"},method = RequestMethod.POST)
	public @ResponseBody ResponseResult detail(HttpServletRequest req) {
		ParamMap param = HttpUtils.getServletRequestParam(req);
		param.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return  userPreferencesSqlFileServiceImpl.selectSqlFileDetail(param);
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
	@RequestMapping(value={"/delete"} ,method = RequestMethod.POST)
	public @ResponseBody ResponseResult delete(@RequestParam(value = "selectItem", required = true )  String selectItem , HttpServletRequest req){
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("selectItem", selectItem);
		paramMap.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return  userPreferencesSqlFileServiceImpl.deleteSqlFile(paramMap);
	}
}
