package com.varsql.app.manager.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.manager.service.DbDiffServiceImpl;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;


/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: DbDiffController.java
* @DESC		: db object 비교
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 12. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/diff")
public class DbDiffController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DbDiffController.class);
	
	@Autowired
	DbDiffServiceImpl dbDiffServiceImpl;
	
	/**
	 * 
	 * @Method Name  : objectType
	 * @Method 설명 : db ojecet type list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 18. 
	 * @변경이력  :
	 * @param vconnid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/objectType"})
	public @ResponseBody ResponseResult objectType(@RequestParam(value = "vconnid") String vconnid ,HttpServletRequest req) throws Exception {
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		
		paramMap.put("vconnid", vconnid);
		
		return dbDiffServiceImpl.objectTypeList(paramMap);
	}
}
