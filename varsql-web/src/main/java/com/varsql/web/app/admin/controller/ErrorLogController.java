package com.varsql.web.app.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.admin.service.ErrorLogServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.PagingUtil;



/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ErrorLogController.java
* @DESC		: error log view controller 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/admin/errorlogMgmt")
public class ErrorLogController extends AbstractController{

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ErrorLogController.class);
	
	@Autowired
	private ErrorLogServiceImpl errorLogServiceImpl; 
	
	
	@RequestMapping({"/list"})
	public @ResponseBody ResponseResult managerlist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return errorLogServiceImpl.selectErrorList(searchParameter);
	}
}
