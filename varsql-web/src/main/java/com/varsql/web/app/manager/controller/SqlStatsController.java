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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.app.manager.service.SqlStatsServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlStatsController.java
* @DESC		: sql 통계
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/stats")
public class SqlStatsController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(SqlStatsController.class);

	@Autowired
	private SqlStatsServiceImpl sqlStatsServiceImpl;
	
	@Autowired
	private ManagerCommonServiceImpl dbnUserServiceImpl;
	
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
	@GetMapping(value = {"", "/","/main"})
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
	 * @Method Name  : dbSqlDateStats
	 * @Method 설명 : 날짜별 통
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param s_date
	 * @param e_date
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbSqlDateStats", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbSqlDateStats(@RequestParam(value = HttpParamConstants.VCONNID, required = true) String vconnid
			,@RequestParam(value = HttpParamConstants.SEARCH_START_DATE, required = true, defaultValue = "" )  String s_date
			,@RequestParam(value = HttpParamConstants.SEARCH_END_DATE, required = true, defaultValue = "" )  String e_date
			) throws Exception {

		return sqlStatsServiceImpl.dbSqlDateStats(vconnid, s_date, e_date);
	}

	/**
	 *
	 * @Method Name  : dbSqlDayStats
	 * @Method 설명 : 일별  sql command count
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param s_date
	 * @param e_date
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbSqlDayStats", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbSqlDayStats(@RequestParam(value = HttpParamConstants.VCONNID, required = true) String vconnid
			,@RequestParam(value = HttpParamConstants.SEARCH_START_DATE, required = true, defaultValue = "" )  String s_date
			,@RequestParam(value = HttpParamConstants.SEARCH_END_DATE, required = true, defaultValue = "" )  String e_date
			) throws Exception {

		return sqlStatsServiceImpl.dbSqlDayStats(vconnid, s_date, e_date);
	}

	/**
	 *
	 * @Method Name  : dbSqlDayUserRank
	 * @Method 설명 : 일 별 사용자  rank
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param s_date
	 * @param e_date
	 * @param command_type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbSqlDayUserRank", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbSqlDayUserRank(@RequestParam(value = HttpParamConstants.VCONNID, required = true) String vconnid
			,@RequestParam(value = HttpParamConstants.SEARCH_START_DATE, required = true, defaultValue = "" )  String s_date
			,@RequestParam(value = HttpParamConstants.SEARCH_END_DATE, required = true, defaultValue = "" )  String e_date
			,@RequestParam(value = "command_type", required = false,defaultValue = "" )  String command_type
			) throws Exception {

		return sqlStatsServiceImpl.dbSqlDayUserRank(vconnid, s_date, e_date ,command_type);
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
	@GetMapping("/history")
	public ModelAndView sqlLogHistory(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "sqlLog");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());

		return getModelAndView("/sqlLogHistory", VIEW_PAGE.MANAGER,model);
	}
	/**
	 * @Method Name  : logList
	 * @Method 설명 :
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 26.
	 * @변경이력  :
	 * @param vconnid
	 * @param s_date
	 * @param e_date
	 * @param command_type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult findSqlLog(@RequestParam(value = HttpParamConstants.VCONNID, required = true) String vconnid, HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return sqlStatsServiceImpl.findSqlLog(vconnid, searchParameter);
	}

}
