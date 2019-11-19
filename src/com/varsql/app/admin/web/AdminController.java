package com.varsql.app.admin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.admin.service.AdminServiceImpl;
import com.varsql.app.common.enums.VIEW_PAGE;
import com.varsql.app.common.web.AbstractController;
import com.vartech.common.utils.HttpUtils;



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
	private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	AdminServiceImpl adminServiceImpl; 
	
	@RequestMapping({"", "/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseMgmt");
		model.addAttribute("dbtype", adminServiceImpl.selectAllDbType());
		return getModelAndView("/databaseMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	@RequestMapping({"/databaseOptMgmt"})
	public ModelAndView databaseOptMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return getModelAndView("/databaseOptMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	@RequestMapping(value = "/report")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "report");
		return getModelAndView("/report", VIEW_PAGE.ADMIN, model);
	}
	
	@RequestMapping(value = "/managerMgmt")
	public ModelAndView managerMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "managerMgmt");
		return getModelAndView("/managerMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	@RequestMapping(value = "/databaseUserMgmt")
	public ModelAndView databaseUserMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseUserMgmt");
		return getModelAndView("/databaseUserMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	@RequestMapping(value = "/userMenuMgmt")
	public ModelAndView userMenuMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "userMenuMgmt");
		model.addAttribute("dbtype", adminServiceImpl.selectAllDbType());
		return getModelAndView("/userMenuMgmt", VIEW_PAGE.ADMIN, model);
	}
	
	@RequestMapping(value = "/errorlogMgmt")
	public ModelAndView errorlogMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "errorlogMgmt");
		return getModelAndView("/errorlogMgmt", VIEW_PAGE.ADMIN, model);
	}
}
