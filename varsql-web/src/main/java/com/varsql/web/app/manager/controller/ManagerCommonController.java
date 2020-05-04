package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerCommonController.java
* @DESC		: 매니저 공통 컨트롤러
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/comm")
public class ManagerCommonController extends AbstractController{

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ManagerCommonController.class);

	@Autowired
	ManagerCommonServiceImpl managerCommonServiceImpl;

	/**
	 *
	 * @Method Name  : dbList
	 * @Method 설명 : 권한 있는 db list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbList"})
	public @ResponseBody ResponseResult dbList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return managerCommonServiceImpl.selectdbList(searchParameter);
	}

	@RequestMapping({"/userList"})
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return managerCommonServiceImpl.selectUserList(searchParameter);
	}
}
