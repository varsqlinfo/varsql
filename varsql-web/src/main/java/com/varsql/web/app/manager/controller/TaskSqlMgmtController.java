package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.app.manager.service.TaskSqlMgmtService;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.task.TaskSqlRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

/**
 * sql task 
 * 
 * @author ytkim
 *
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/task/sql")
public class TaskSqlMgmtController extends AbstractController {
	

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(TaskSqlMgmtController.class);
	
	private final TaskSqlMgmtService taskSqlMgmtService;
	
	private final ManagerCommonServiceImpl dbnUserServiceImpl;
	
	/**
	 * sql task 관리 페이지 보기
	 * 
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"","/"})
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "taskMgmt");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());
		
		return getModelAndView("/sqlTaskMgmt", VIEW_PAGE.MANAGER_TASK,model);
	}
	
	/**
	 * 목록 얻기
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@PostMapping({"/list" })
	public @ResponseBody ResponseResult list(HttpServletRequest req,
			HttpServletResponse res, ModelAndView mav) throws Exception {
		
		return taskSqlMgmtService.list(HttpUtils.getSearchParameter(req));
	}
	
	/**
	 * 정보 저장.
	 *
	 * @method : save
	 * @param dto
	 * @param result
	 * @param mav
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping({"/save" })
	public @ResponseBody ResponseResult save(@Valid TaskSqlRequestDTO dto, BindingResult result, ModelAndView mav, HttpServletRequest req) throws Exception {
		
		ResponseResult resultObject = new ResponseResult();
		if (result.hasErrors()) {
			for (ObjectError errorVal : result.getAllErrors()) {
				logger.warn("###  changeLogItemInfo validation check {}", errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(resultObject, result);
		}
		    
		return taskSqlMgmtService.save(dto);
	}
	
	/**
	 * 정보 삭제.
	 *
	 * @method : remove
	 * @param taskId
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping({"/remove" })
	public @ResponseBody ResponseResult remove(@RequestParam(value = "taskId", required = true) String taskId, HttpServletRequest req) throws Exception {
		return taskSqlMgmtService.remove(taskId);
	}
	
	/**
	 * 복사
	 *
	 * @param taskId
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping({"/copy" })
	public @ResponseBody ResponseResult copy(@RequestParam(value = "taskId", required = true) String taskId, HttpServletRequest req) throws Exception {
		return taskSqlMgmtService.copyInfo(taskId);
	}
	
	/**
	 * sql task 실행
	 * 
	 * @param taskId
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping({"/execute" })
	public @ResponseBody ResponseResult execute(@RequestParam(value = "taskId", required = true) String taskId, HttpServletRequest req) throws Exception {
		DataMap param = HttpUtils.getServletRequestParam(req);
		
		logger.info("task execute taskId: {}, ip: {} ", taskId, VarsqlUtils.getClientIp(req));
		
		return taskSqlMgmtService.execute(taskId, param.getString(HttpParamConstants.REQ_UID));
	}
	
	/**
	 * task 이력 조회
	 * 
	 * @param taskId
	 * @param req
	 * @return
	 */
	@PostMapping("/history")
	public @ResponseBody ResponseResult history(@RequestParam(value = "taskId" , required = true) String taskId, HttpServletRequest req){
		return taskSqlMgmtService.findHistory(taskId, HttpUtils.getSearchParameter(req));
	}
}
