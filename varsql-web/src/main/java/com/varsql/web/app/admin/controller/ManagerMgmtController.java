package com.varsql.web.app.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.auth.AuthorityTypeImpl;
import com.varsql.web.app.admin.service.ManagerMgmtServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerMgmtController.java
* @DESC		: 매니저 관리
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/admin/managerMgmt")
@RequiredArgsConstructor
public class ManagerMgmtController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(ManagerMgmtController.class);

	private final ManagerMgmtServiceImpl managerMgmtServiceImpl;

	/**
	 * @method  : userList
	 * @desc : 사용자 권한 목록 보기.
	 * @author   : ytkim
	 * @date   : 2020. 4. 21.
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/userList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return managerMgmtServiceImpl.searchRoleUserList(AuthorityTypeImpl.USER , searchParameter);
	}

	/**
	 * @method  : managerlist
	 * @desc : 매니저 권한 목록 보기
	 * @author   : ytkim
	 * @date   : 2020. 4. 21.
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/managerList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult managerlist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return managerMgmtServiceImpl.searchRoleUserList(AuthorityTypeImpl.MANAGER , searchParameter);
	}

	@RequestMapping(value = "/managerRoleMgmt", method = RequestMethod.POST)
	public @ResponseBody ResponseResult managerRoleMgmt(@RequestParam(value = "mode", required = true, defaultValue = "del" )  String mode
			,@RequestParam(value = "viewid", required = true)  String viewid
			) throws Exception {
		
		logger.debug("managerRoleMgmt mode: {} , viewid : {}", mode, viewid);
		
		return managerMgmtServiceImpl.updateManagerRole(mode , viewid);
	}

	/**
	 * @method  : addDbManager
	 * @desc : db 매니저 추가 삭제.
	 * @author   : ytkim
	 * @date   : 2020. 4. 22.
	 * @param selectItem
	 * @param vconnid
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dbManagerMapping", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbManagerMapping(@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "vconnid", required = true) String vconnid
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			) throws Exception {

		return managerMgmtServiceImpl.updateDbManager(selectItem, vconnid, mode);
	}

	@RequestMapping(value="/dbManagerList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbManagerList(@RequestParam(value = "vconnid", required = true) String vconnid) throws Exception {
		return managerMgmtServiceImpl.findDatabaseManager(vconnid);
	}
}
