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

import com.varsql.web.app.manager.service.SqlJobServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.scheduler.JobRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * sql job
* 
* @fileName	: SqlJobMgmtController.java
* @author	: ytkim
 */
@Controller
@RequestMapping("/manager/sqlJob")
public class SqlJobMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(SqlJobMgmtController.class);

	@Autowired
	private SqlJobServiceImpl sqlJobServiceImpl;
	
	/**
	 * job list
	 *
	 * @method : list
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/list")
	public @ResponseBody ResponseResult list(HttpServletRequest req) {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return sqlJobServiceImpl.findJobList(searchParameter);
	}
	
	@PostMapping("/save")
	public @ResponseBody ResponseResult save(@Valid JobRequestDTO jobRequestDTO, BindingResult result,HttpServletRequest req) {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("### save check {}", errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		return sqlJobServiceImpl.save(jobRequestDTO);
	}
}
