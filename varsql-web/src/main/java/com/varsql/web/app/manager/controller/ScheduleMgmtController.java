package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.manager.service.ScheduleMgmtServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;



/**
 * schedule management
* 
* @fileName	: ScheduleMgmtController.java
* @author	: ytkim
 */
@Controller
@RequestMapping("/manager/scheduleMgmt")
public class ScheduleMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(ScheduleMgmtController.class);

	@Autowired
	private ScheduleMgmtServiceImpl scheduleMgmtServiceImpl;
	
	/**
	 * job 상세보기.
	 *
	 * @method : detail
	 * @param viewid
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/detail")
	public @ResponseBody ResponseResult detail(@RequestParam(value = "jobUid", required = true )  String jobUid) {
		return scheduleMgmtServiceImpl.findDetailInfo(jobUid);
	}
	
	/**
	 * job 삭제. 
	 *
	 * @method : delete
	 * @param jobUid
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/delete")
	public @ResponseBody ResponseResult delete(@RequestParam(value = "jobUid" , required = true) String jobUid, HttpServletRequest req) {
		logger.info("job delete viewid: {}, ip: {} , jobUid: {}, mode: {}", SecurityUtil.userViewId(), VarsqlUtils.getClientIp(req), jobUid);
		return scheduleMgmtServiceImpl.delete(jobUid);
	}
	
	@PostMapping("/jobCtrl")
	public @ResponseBody ResponseResult jobCtrl(@RequestParam(value = "jobUid" , required = true) String jobUid
			,@RequestParam(value = "mode" , required = true) String mode, HttpServletRequest req) {
		
		logger.info("job ctrl viewid: {}, ip: {} , jobUid: {}, mode: {}", SecurityUtil.userViewId(), VarsqlUtils.getClientIp(req), jobUid, mode);
		
		return scheduleMgmtServiceImpl.jobCtrl(jobUid, mode);
	}
	/**
	 * 
	 *
	 * @method : history
	 * @param jobUid
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/history")
	public @ResponseBody ResponseResult history(@RequestParam(value = "jobUid" , required = true) String jobUid, HttpServletRequest req){
		return scheduleMgmtServiceImpl.findHistory(jobUid, HttpUtils.getSearchParameter(req));
	}
	
}
