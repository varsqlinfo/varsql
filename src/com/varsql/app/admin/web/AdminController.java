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
public class AdminController{

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	AdminServiceImpl adminServiceImpl; 
	
	@RequestMapping({"", "/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseMgmt");
		model.addAttribute("dbtype", adminServiceImpl.selectAllDbType());
		return  new ModelAndView("/admin/databaseMgmt",model);
	}
	
	@RequestMapping({"/databaseOptMgmt"})
	public ModelAndView databaseOptMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return  new ModelAndView("/admin/databaseOptMgmt",model);
	}
	
	@RequestMapping(value = "/report")
	public ModelAndView report(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "report");
		return new ModelAndView("/admin/report",model);
	}
	
	@RequestMapping(value = "/managerMgmt")
	public ModelAndView managerMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "managerMgmt");
		return new ModelAndView("/admin/managerMgmt",model);
	}
	
	@RequestMapping(value = "/databaseUserMgmt")
	public ModelAndView databaseUserMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "databaseUserMgmt");
		return new ModelAndView("/admin/databaseUserMgmt",model);
	}
	
	@RequestMapping(value = "/userMenuMgmt")
	public ModelAndView userMenuMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "userMenuMgmt");
		model.addAttribute("dbtype", adminServiceImpl.selectAllDbType());
		return new ModelAndView("/admin/userMenuMgmt",model);
	}
	
	@RequestMapping(value = "/errorlogMgmt")
	public ModelAndView errorlogMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "errorlogMgmt");
		return new ModelAndView("/admin/errorlogMgmt",model);
	}
}
