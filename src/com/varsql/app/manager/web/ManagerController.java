package com.varsql.app.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.UserConstants;
import com.varsql.app.manager.service.DbnUserServiceImpl;
import com.varsql.app.manager.service.ManagerServiceImpl;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.core.common.util.SecurityUtil;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
	
	@Autowired
	ManagerServiceImpl managerServiceImpl;
	
	@Autowired
	DbnUserServiceImpl dbnUserServiceImpl;

	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/manager/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/manageMain", model);
	}
	
	@RequestMapping(value="/dbUserMgmt")
	public ModelAndView dbUserMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/dbUserMgmt");
	}
	
	@RequestMapping({"/qnaMgmt"})
	public ModelAndView qnaMgmtList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/qnaMgmt",model);
	}
	
	@RequestMapping({"/glossaryMgmt"})
	public ModelAndView glossaryMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/glossaryMgmt",model);
	}
	
	@RequestMapping({"/sqlLogStat"})
	public ModelAndView sqlLogStat(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		model.addAttribute("startDate", DateUtils.getCalcDate(-7));
		model.addAttribute("currentDate", DateUtils.getCurrentDate());
		
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		searchParameter.addCustomParam("allYn", "Y");
		
		model.addAttribute("dbList", dbnUserServiceImpl.selectUserdbList(searchParameter));
		
		return new ModelAndView("/manager/sqlLogStat",model);
	}
	
	@RequestMapping({"/sqlLogHistory"})
	public ModelAndView sqlLogHistory(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		searchParameter.addCustomParam("allYn", "Y");
		
		model.addAttribute("dbList", dbnUserServiceImpl.selectUserdbList(searchParameter));
		
		return new ModelAndView("/manager/sqlLogHistory",model);
	}
	
	@RequestMapping({"/userList"})
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		return managerServiceImpl.selectUserList(searchParameter);
	}
	
	@RequestMapping({"/acceptYn"})
	public @ResponseBody ResponseResult updAcceptYn(@RequestParam(value = "acceptyn", required = true )  String acceptyn
			,@RequestParam(value = "selectItem", required = true )  String selectItem
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("acceptyn", acceptyn);
		paramMap.put("selectItem", selectItem);
		
		return managerServiceImpl.updateAccept(paramMap);
	}
	
	@RequestMapping({"/initPassword"})
	public @ResponseBody ResponseResult initPassword(@RequestParam(value = "VIEWID", required = true )  String viewid) throws Exception {
		
		PasswordForm userForm = new PasswordForm();
		
		userForm.setViewid(viewid);
		
		return managerServiceImpl.initPassword(userForm);
	}
}
