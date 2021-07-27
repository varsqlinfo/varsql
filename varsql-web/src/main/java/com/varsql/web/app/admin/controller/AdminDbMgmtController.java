package com.varsql.web.app.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.admin.service.AdminServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.dto.db.DBConnectionRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: AdminDbMgmtController.java
* @DESC		: admin db 관리
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 8. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/admin/main")
public class AdminDbMgmtController extends AbstractController{

	/** The Constant logger. */
	private final Logger logger = LoggerFactory.getLogger(AdminDbMgmtController.class);

	@Autowired
	private AdminServiceImpl adminServiceImpl;

	/**
	 *
	 * @Method Name  : dblist
	 * @작성일   : 2014. 8. 18.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db 목록 보기
	 * @param searchVal
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dblist", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dblist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return adminServiceImpl.selectDblist(searchParameter);
	}

	/**
	 *
	 * @Method Name  : dbDriver
	 * @Method 설명 : select db driver
	 * @작성자   : ytkim
	 * @작성일   : 2017. 5. 25.
	 * @변경이력  :
	 * @param dbtype
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/dbDriver")
	public @ResponseBody ResponseResult dbDriver(@RequestParam(value = "dbtype", required = true)  String dbtype) throws Exception {
		return adminServiceImpl.selectDbDriverList(dbtype);
	}

	/**
	 *
	 * @Method Name  : dbDetail
	 * @작성일   : 2014. 8. 18.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db정보 상세보기
	 * @param vconnid
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/dbDetail")
	public @ResponseBody ResponseResult dbDetail(@RequestParam(value = "vconnid" , required = true) String vconnid) throws Exception {
		return adminServiceImpl.selectDetailObject(vconnid);
	}

	/**
	 *
	 * @Method Name  : dbConnectionCheck
	 * @작성일   : 2014. 8. 18.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 :커넥션 체크
	 * @param vconnid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbConnectionCheck", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbConnectionCheck(@Valid DBConnectionRequestDTO vtConnection, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			resultObject = VarsqlUtils.getResponseResultValidItem(resultObject, result);
		}else{
			resultObject = adminServiceImpl.connectionCheck(vtConnection);
		}

		return resultObject;
	}

	@RequestMapping(value = "/dbConnectionClose", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbConnectionClose(@RequestParam(value = "vconnid" , required = true)  String vconnid) throws Exception {
		logger.debug("dbConnectionClose : {}" , vconnid);
		return adminServiceImpl.connectionClose(vconnid);
	}

	@RequestMapping(value = "/dbConnectionReset", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbConnectionReset(@RequestParam(value = "vconnid" , required = true)  String vconnid) throws Exception {
		logger.debug("dbConnectionReset : {}" , vconnid);
		return adminServiceImpl.dbConnectionReset(vconnid);
	}
	
	@RequestMapping(value = "/dbConnectionCopy", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbConnectionCopy(@RequestParam(value = "vconnid" , required = true)  String vconnid) throws Exception {
		return adminServiceImpl.dbConnectionCopy(vconnid);
	}

	/**
	 *
	 * @Method Name  : dbSave
	 * @작성일   : 2014. 8. 18.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : 정보 저장
	 * @param vconnid
	 * @param vname
	 * @param vurl
	 * @param vdriver
	 * @param vtype
	 * @param vid
	 * @param vpw
	 * @param vconnopt
	 * @param vpoolopt
	 * @param vquery
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbSave", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbSave(@Valid DBConnectionRequestDTO vtConnection, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			resultObject = VarsqlUtils.getResponseResultValidItem(resultObject, result);
		}else{
			resultObject = adminServiceImpl.saveVtconnectionInfo(vtConnection);
		}

		return  resultObject;
	}

	/**
	 *
	 * @Method Name  : dbDelete
	 * @작성일   : 2014. 8. 18.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @Method 설명 : db 정보 삭제
	 * @param vconnid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dbDelete", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbDelete(@RequestParam(value = "vconnid" , required = true)  String vconnid) throws Exception {
		return VarsqlUtils.getResponseResultItemOne(adminServiceImpl.deleteVtconnectionInfo(vconnid));
	}
	
	@RequestMapping(value = "/dbPwView", method = RequestMethod.POST)
	public @ResponseBody ResponseResult dbPwView(@RequestParam(value = "vconnid", required = true)  String vconnid
			,@RequestParam(value = "userPw", required = true)  String userPw) throws Exception {
		return adminServiceImpl.viewVtConntionPwInfo(vconnid, userPw);
	}
}
