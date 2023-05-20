package com.varsql.web.app.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.manager.service.DbDiffServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.vartech.common.app.beans.ResponseResult;


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
public class DbDiffController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(DbDiffController.class);

	@Autowired
	private DbDiffServiceImpl dbDiffServiceImpl;

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
	@RequestMapping(value = "/objectType", method = RequestMethod.POST)
	public @ResponseBody ResponseResult objectType(@RequestParam(value = "vconnid", required = true) String vconnid ,HttpServletRequest req) throws Exception {

		return dbDiffServiceImpl.objectTypeList(vconnid);
	}

	@RequestMapping(value = "/objectList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult objectList(@RequestParam(value = "vconnid", required = true) String vconnid
			,@RequestParam(value = "objectType" ,required = true) String objectType
			,@RequestParam(value = "schema" ,required = true) String schema	) throws Exception {

		return dbDiffServiceImpl.objectList(vconnid, objectType, schema);
	}
}
