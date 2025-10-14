package com.varsql.web.app.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.admin.service.AdminDbMgmtServiceImpl;
import com.varsql.web.app.admin.service.EnvironmentViewServiceImpl;
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
	private AdminDbMgmtServiceImpl adminDbMgmtServiceImpl;
	
	@Autowired
	private EnvironmentViewServiceImpl environmentViewServiceImpl;

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
	@RequestMapping(value = {"", "/","/databaseMgmt"}, method = RequestMethod.GET)
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		logger.debug("admin mainpage");
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseMgmt");
		model.addAttribute("jdbcUrlFormat", VartechUtils.objectToJsonString(this.adminDbMgmtServiceImpl.dbTypeUrlFormat()));
		return getModelAndView("/databaseMgmt", VIEW_PAGE.ADMIN, model);
	}

	/**
	 * 매니저 관리
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/managerMgmt")
	public ModelAndView managerMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "managerMgmt");
		return getModelAndView("/managerMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	/**
	 * database manager 관리
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/databaseManagerMgmt")
	public ModelAndView databaseManagerMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseManagerMgmt");
		return getModelAndView("/databaseManagerMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	
	/**
	 * error 로그 관리
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/errorlogMgmt")
	public ModelAndView errorlogMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "errorlogMgmt");
		return getModelAndView("/errorlogMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	/**
	 * jdbc 드라이버 관리
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/driverMgmt")
	public ModelAndView driverMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav)
			throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "driverMgmt");
		model.addAttribute("dbtype", this.adminDbMgmtServiceImpl.selectAllDbType());
		return getModelAndView("/driverMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	/**
	 * 사용자 메뉴 관리
	 * 
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/userMenuMgmt")
	public ModelAndView userMenuMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		// TO DO
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "userMenuMgmt");
		model.addAttribute("dbtype", adminDbMgmtServiceImpl.selectAllDbType());
		return getModelAndView("/userMenuMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	@GetMapping(value = "/report")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		// TO DO
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "report");
		return getModelAndView("/report", VIEW_PAGE.ADMIN, model);
	}
	
	/**
	 * varsql config view
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/appEnv")
	public ModelAndView appEnv(HttpServletRequest req,	HttpServletResponse res, ModelAndView mav) throws Exception {
		
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "env");
		model.addAttribute("configInfo", VartechUtils.objectToJsonString(environmentViewServiceImpl.appConfigInfo()));
		
		return getModelAndView("/appEnv", VIEW_PAGE.ADMIN_ENV, model);
	}
	
	/**
	 * system 정보 보기
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/systemInfo")
	public ModelAndView systemInfo(HttpServletRequest req,	HttpServletResponse res, ModelAndView mav) throws Exception {
		
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "env");
		model.addAttribute("configInfo", VartechUtils.objectToJsonString(environmentViewServiceImpl.systemInfo()));
		
		return getModelAndView("/systemInfo", VIEW_PAGE.ADMIN_ENV, model);
	}
}
