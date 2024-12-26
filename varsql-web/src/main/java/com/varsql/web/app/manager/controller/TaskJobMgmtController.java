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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.app.manager.service.TaskJobServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.scheduler.JobRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * task job
* 
* @fileName	: TaskJobMgmtController.java
* @author	: ytkim
 */
@Controller
@RequestMapping("/manager/taskJob")
public class TaskJobMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(TaskJobMgmtController.class);

	@Autowired
	private TaskJobServiceImpl taskJobServiceImpl;
	
	@Autowired
	private ManagerCommonServiceImpl managerCommonServiceImpl;


	/**
	 * task job 관리
	 *
	 * @method : taskJobMgmt
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = {"", "/","/main"})
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "jobMgmt");
		model.addAttribute("dbList", managerCommonServiceImpl.selectdbList());
		
		return getModelAndView("/taskJobMgmt", VIEW_PAGE.MANAGER_SCHEDULER,model);
	}
	
	/**
	 * task job list
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/list")
	public @ResponseBody ResponseResult list(HttpServletRequest req) {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return taskJobServiceImpl.findJobList(searchParameter);
	}
	
	@PostMapping("/save")
	public @ResponseBody ResponseResult save(@Valid JobRequestDTO jobRequestDTO, BindingResult result,HttpServletRequest req) {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("### save check {}", errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return taskJobServiceImpl.save(jobRequestDTO);
	}
	
	/**
	 * task list
	 * @param req
	 * @return
	 */
	@PostMapping("/taskList")
	public @ResponseBody ResponseResult taskList(HttpServletRequest req) {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return taskJobServiceImpl.taskList(searchParameter);
	}
}
