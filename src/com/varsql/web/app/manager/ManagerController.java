package com.varsql.web.app.manager;

import java.util.Map;

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

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.DateUtil;
import com.varsql.web.util.HttpUtil;



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

	@RequestMapping({""})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/manager/");
	}
	
	@RequestMapping({"/","/main"})
	public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/manageMain", model);
	}
	
	@RequestMapping(value="/dbUserMgmt")
	public ModelAndView dbUserMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/dbUserMgmt");
	}
	
	@RequestMapping({"/qnaMgmt"})
	public ModelAndView qnaMgmtList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/qnaMgmt",model);
	}
	
	@RequestMapping({"/sqlLogStat"})
	public ModelAndView sqlLogStat(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		model.addAttribute("startDate", DateUtil.getCurrentDayCalc(-7));
		model.addAttribute("currentDate", DateUtil.getCurrentDate());
		return new ModelAndView("/manager/sqlLogStat",model);
	}
	
	@RequestMapping({"/userLogStat"})
	public ModelAndView userLogStat(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtil.getOriginatingRequestUri(req));
		return new ModelAndView("/manager/userLogStat",model);
	}
	
	@RequestMapping({"/userList"})
	public @ResponseBody Map userList(@RequestParam(value = "searchval", required = false, defaultValue = "" )  String searchval
			,@RequestParam(value = "page", required = false, defaultValue = "1" )  int page
			,@RequestParam(value = "rows", required = false, defaultValue = "10" )  int rows
		) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("page", page);
		paramMap.put("rows", rows);
		paramMap.put("searchval", searchval);
		
		return managerServiceImpl.selectUserList(paramMap);
	}
	
	@RequestMapping({"/acceptYn"})
	public @ResponseBody Map updAcceptYn(@RequestParam(value = "acceptyn", required = true )  String acceptyn
			,@RequestParam(value = "selectItem", required = true )  String selectItem
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("acceptyn", acceptyn);
		paramMap.put("selectItem", selectItem);
		
		return managerServiceImpl.updateAccept(paramMap);
	}
}
