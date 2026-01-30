package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.DbDiffServiceImpl;
import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.vartech.common.app.beans.ResponseResult;

import lombok.RequiredArgsConstructor;


/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbDiffController.java
* @DESC		: db object 비교
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 12. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/diff")
@RequiredArgsConstructor
public class DbDiffController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DbDiffController.class);

	private final DbDiffServiceImpl dbDiffServiceImpl;
	
	private final ManagerCommonServiceImpl dbnUserServiceImpl;
	
	/**
	 *
	 * @Method Name  : dbCompareMgmt
	 * @Method 설명 : db 비교.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView dbCompareMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbCompareMgmt");

		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());

		return getModelAndView("/dbCompareMgmt", VIEW_PAGE.MANAGER,model);
	}

	/**
	 *
	 * @Method Name  : objectType
	 * @Method 설명 : db ojecet type list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 18.
	 * @변경이력  :
	 * @param vconnid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/objectType", method = RequestMethod.POST)
	public @ResponseBody ResponseResult objectType(@RequestParam(value = "vconnid", required = true) String vconnid ,HttpServletRequest req) throws Exception {

		return dbDiffServiceImpl.objectTypeList(vconnid);
	}
}
