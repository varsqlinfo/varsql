package com.varsql.web.app.plugin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.plugin.service.PluginServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PluginController.java
* @DESC		: 플로그인  controller
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/plugin")
public class PluginController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(PluginController.class);

	@Autowired
	private PluginServiceImpl pluginServiceImpl;

	/**
	 *
	 * @Method Name  : glossarySearch
	 * @Method 설명 : 용어 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 24.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/glossary/search", method = RequestMethod.POST)
	public @ResponseBody ResponseResult glossarySearch(HttpServletRequest req) throws Exception {
		SearchParameter param = HttpUtils.getSearchParameter(req);

		return pluginServiceImpl.glossarySearch(param);
	}

	/**
	 *
	 * @Method Name  : historySearch
	 * @Method 설명 : sql history search.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 26.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/history/search", method = RequestMethod.POST)
	public @ResponseBody ResponseResult historySearch(HttpServletRequest req) throws Exception {
		SearchParameter param = HttpUtils.getSearchParameter(req);

		param.addCustomParam(HttpParamConstants.CONN_UUID, SecurityUtil.getVconnid(String.valueOf(param.getCustomParam().get(HttpParamConstants.CONN_UUID))));
		param.addCustomParam(HttpParamConstants.UID, SecurityUtil.userViewId());

		return pluginServiceImpl.historySearch(param);
	}
}
