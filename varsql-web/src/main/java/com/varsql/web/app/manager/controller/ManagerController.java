package com.varsql.web.app.manager.controller;

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

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerController.java
* @DESC		: 매니저 main
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	@Autowired
	private ManagerCommonServiceImpl dbnUserServiceImpl;

	@GetMapping(value = {"", "/","/main"})
	public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "userMgmt");
		return getModelAndView("/userMgmt", VIEW_PAGE.MANAGER,model);
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
	@GetMapping("/qnaMgmt")
	public ModelAndView qnaMgmtList(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "qnaMgmt");
		return getModelAndView("/qnaMgmt", VIEW_PAGE.MANAGER,model);
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
	@GetMapping("/glossaryMgmt")
	public ModelAndView glossaryMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "glossaryMgmt");
		return getModelAndView("/glossaryMgmt", VIEW_PAGE.MANAGER,model);
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
	@GetMapping("/dbGroupMgmt")
	public ModelAndView dbGroupMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbGroupMgmt");
		return getModelAndView("/dbGroupMgmt", VIEW_PAGE.MANAGER,model);
	}

	/**
	 *
	 * @Method Name  : dbUserMgmt
	 * @Method 설명 : db 그룹 사용자 관리.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/dbGroupUserMgmt")
	public ModelAndView dbUserMgmt(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbGroupMgmt");
		return getModelAndView("/dbGroupUserMgmt", VIEW_PAGE.MANAGER,model);
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
	@GetMapping("/dbCompareMgmt")
	public ModelAndView dbCompareMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "dbCompareMgmt");

		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());

		return getModelAndView("/dbCompareMgmt", VIEW_PAGE.MANAGER,model);
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
	@GetMapping("/sqlLogStat")
	public ModelAndView sqlLogStat(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "sqlLog");
		model.addAttribute("startDate", VarsqlDateUtils.calcDateFormat(-7, VarsqlDateUtils.DateCheckType.DAY, VarsqlConstants.DATE_FORMAT));
		model.addAttribute("currentDate", VarsqlDateUtils.currentDateFormat());

		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());

		return getModelAndView("/sqlLogStat", VIEW_PAGE.MANAGER,model);
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
	@GetMapping("/sqlLogHistory")
	public ModelAndView sqlLogHistory(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "sqlLog");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());

		return getModelAndView("/sqlLogHistory", VIEW_PAGE.MANAGER,model);
	}
	
	/**
	 * 데이터 백업 관리.
	 *
	 * @method : dataBackupMgmt
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/dataBackupMgmt")
	public ModelAndView dataBackupMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "backupMgmt");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());
		
		return getModelAndView("/dataBackupMgmt", VIEW_PAGE.MANAGER,model);
	}
	
	/**
	 * ddl backpu 관리
	 *
	 * @method : ddlBackupMgmt
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/ddlBackupMgmt")
	public ModelAndView ddlBackupMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "backupMgmt");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());
		
		return getModelAndView("/ddlBackupMgmt", VIEW_PAGE.MANAGER,model);
	}
}
