package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.DDLBackupMgmtServiceImpl;
import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.scheduler.JobRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * DDL backup
* 
* @fileName	: DDLBackupMgmtController.java
* @author	: ytkim
 */
@Controller
@RequestMapping("/manager/ddlBackup")
public class DDLBackupMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DDLBackupMgmtController.class);

	@Autowired
	private DDLBackupMgmtServiceImpl ddlBackupMgmtServiceImpl;
	
	@Autowired
	private ManagerCommonServiceImpl dbnUserServiceImpl;
	
	/**
	 * ddl backup 관리
	 *
	 * @method : ddlBackupMgmt
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView ddlBackupMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "jobMgmt");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());
		
		return getModelAndView("/ddlBackupMgmt", VIEW_PAGE.MANAGER_SCHEDULER, model);
	}
	
	/**
	 * backup list
	 *
	 * @method : list
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/list")
	public @ResponseBody ResponseResult list(HttpServletRequest req) {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return ddlBackupMgmtServiceImpl.findDataBackupJobList(searchParameter);
	}
	
	@PostMapping("/dataObjectList")
	public @ResponseBody ResponseResult dataObjectList(@RequestParam(value = "vconnid", required = true) String vconnid, HttpServletRequest req) {
		return ddlBackupMgmtServiceImpl.dataObjectList(vconnid);
	}
	
	@PostMapping("/save")
	public @ResponseBody ResponseResult save(@Valid JobRequestDTO jobRequestDTO, BindingResult result,HttpServletRequest req) {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("### save check {}", errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return ddlBackupMgmtServiceImpl.save(jobRequestDTO);
	}
}
