package com.varsql.web.app.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.admin.service.AdminDbMgmtServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechUtils;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: AdminController.java
* @DESC		: admin 관리
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends AbstractController{

	/** The Constant logger. */
	private final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminDbMgmtServiceImpl adminServiceImpl;

	/**
	 * @method  : mainpage
	 * @desc : admin main
	 * @author   : ytkim
	 * @date   : 2020. 4. 14.
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"", "/","/main"}, method = RequestMethod.GET)
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		logger.debug("admin mainpage");
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseMgmt");
		model.addAttribute("jdbcUrlFormat", VartechUtils.objectToJsonString(this.adminServiceImpl.dbTypeUrlFormat()));
		return getModelAndView("/databaseMgmt", VIEW_PAGE.ADMIN, model);
	}

	/**
	 * @method  : databaseOptMgmt
	 * @desc : database option management
	 * @author   : ytkim
	 * @date   : 2020. 4. 14.
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ={"/databaseOptMgmt"}, method = RequestMethod.GET)
	public ModelAndView databaseOptMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return getModelAndView("/databaseOptMgmt", VIEW_PAGE.ADMIN, model);
	}

	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "report");
		return getModelAndView("/report", VIEW_PAGE.ADMIN, model);
	}

	@RequestMapping(value = "/managerMgmt", method = RequestMethod.GET)
	public ModelAndView managerMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "managerMgmt");
		return getModelAndView("/managerMgmt", VIEW_PAGE.ADMIN, model);
	}

	@RequestMapping(value = "/databaseUserMgmt", method = RequestMethod.GET)
	public ModelAndView databaseUserMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseUserMgmt");
		return getModelAndView("/databaseUserMgmt", VIEW_PAGE.ADMIN, model);
	}

	@RequestMapping(value = "/userMenuMgmt", method = RequestMethod.GET)
	public ModelAndView userMenuMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "userMenuMgmt");
		model.addAttribute("dbtype", adminServiceImpl.selectAllDbType());
		return getModelAndView("/userMenuMgmt", VIEW_PAGE.ADMIN, model);
	}

	@RequestMapping(value = "/errorlogMgmt", method = RequestMethod.GET)
	public ModelAndView errorlogMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "errorlogMgmt");
		return getModelAndView("/errorlogMgmt", VIEW_PAGE.ADMIN, model);
	}

	@RequestMapping(value = { "/driverMgmt" }, method = { RequestMethod.GET })
	public ModelAndView driverMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav)
			throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "driverMgmt");
		model.addAttribute("dbtype", this.adminServiceImpl.selectAllDbType());
		return getModelAndView("/driverMgmt", VIEW_PAGE.ADMIN, model);
	}
}
