package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.manager.service.DataBackupMgmtServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.scheduler.JobScheduleRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * data backup 
* 
* @fileName	: DataBackupMgmtController.java
* @author	: ytkim
 */
@Controller
@RequestMapping("/manager/dataBackup")
public class DataBackupMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DataBackupMgmtController.class);

	@Autowired
	private DataBackupMgmtServiceImpl dataBackupMgmtServiceImpl;
	
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

		return dataBackupMgmtServiceImpl.findDataBackupJobList(searchParameter);
	}
	
	@PostMapping("/dataObjectList")
	public @ResponseBody ResponseResult dataObjectList(@RequestParam(value = "vconnid", required = true) String vconnid, HttpServletRequest req) {
		return dataBackupMgmtServiceImpl.dataObjectList(vconnid);
	}
	
	@PostMapping("/save")
	public @ResponseBody ResponseResult save(@Valid JobScheduleRequestDTO jobScheduleRequestDTO, BindingResult result,HttpServletRequest req) {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("### save check {}", errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return dataBackupMgmtServiceImpl.save(jobScheduleRequestDTO);
	}
}
