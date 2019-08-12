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
import com.varsql.app.manager.service.UserMgmtServiceImpl;
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
	DbnUserServiceImpl dbnUserServiceImpl;
	
	@RequestMapping({"", "/","/main"})
	public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "main");
		return new ModelAndView("/manager/manageMain", model);
	}
	
	@RequestMapping(value="/dbUserMgmt")
	public ModelAndView dbUserMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbUserMgmt");
		return new ModelAndView("/manager/dbUserMgmt", model);
	}
	
	/**
	 * 
	 * @Method Name  : qnaMgmtList
	 * @Method 설명 : Q & A
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/qnaMgmt"})
	public ModelAndView qnaMgmtList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "qnaMgmt");
		return new ModelAndView("/manager/qnaMgmt",model);
	}
	
	/**
	 * 
	 * @Method Name  : glossaryMgmt
	 * @Method 설명 : 용어집
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/glossaryMgmt"})
	public ModelAndView glossaryMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "glossaryMgmt");
		return new ModelAndView("/manager/glossaryMgmt",model);
	}
	/**
	 * 
	 * @Method Name  : dbGroupMgmt
	 * @Method 설명 : db 그룹 관리
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbGroupMgmt"})
	public ModelAndView dbGroupMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbGroupMgmt");
		return new ModelAndView("/manager/dbGroupMgmt",model);
	}
	
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
	@RequestMapping({"/dbCompareMgmt"})
	public ModelAndView dbCompareMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbCompareMgmt");
		
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		searchParameter.addCustomParam("allYn", "Y");
		
		model.addAttribute("dbList", dbnUserServiceImpl.selectUserdbList(searchParameter));
		
		return new ModelAndView("/manager/dbCompareMgmt",model);
	}
	
	/**
	 * 
	 * @Method Name  : sqlLogStat
	 * @Method 설명 : sql log  통계. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sqlLogStat"})
	public ModelAndView sqlLogStat(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "sqlLog");
		model.addAttribute("startDate", DateUtils.getCalcDate(-7));
		model.addAttribute("currentDate", DateUtils.getCurrentDate());
		
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		searchParameter.addCustomParam("allYn", "Y");
		
		model.addAttribute("dbList", dbnUserServiceImpl.selectUserdbList(searchParameter));
		
		return new ModelAndView("/manager/sqlLogStat",model);
	}
	
	/**
	 * 
	 * @Method Name  : sqlLogHistory
	 * @Method 설명 : sql 이력 조회.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9. 
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sqlLogHistory"})
	public ModelAndView sqlLogHistory(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "sqlLog");
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		searchParameter.addCustomParam("allYn", "Y");
		
		model.addAttribute("dbList", dbnUserServiceImpl.selectUserdbList(searchParameter));
		
		return new ModelAndView("/manager/sqlLogHistory",model);
	}
}
