package com.varsql.app.admin.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.app.admin.service.ManagerMgmtServiceImpl;
import com.varsql.app.common.beans.DataCommonVO;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/admin/managerMgmt")
public class ManagerMgmtController{

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ManagerMgmtController.class);
	
	@Autowired
	ManagerMgmtServiceImpl managerMgmtServiceImpl; 
	
	@RequestMapping({"/userList"})
	public @ResponseBody ResponseResult userList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		return managerMgmtServiceImpl.selectRoleUserList(searchParameter);
	}
	
	@RequestMapping({"/managerList"})
	public @ResponseBody ResponseResult managerlist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		return managerMgmtServiceImpl.selectRoleManagerList(searchParameter);
	}
	
	@RequestMapping({"/managerRoleMgmt"})
	public @ResponseBody ResponseResult managerRoleMgmt(@RequestParam(value = "mode", required = true, defaultValue = "del" )  String mode
			,@RequestParam(value = "viewid", required = true)  String viewid
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("mode", mode);
		paramMap.put("viewid", viewid);
		
		return managerMgmtServiceImpl.updateManagerRole(paramMap);
	}
	
	@RequestMapping({"/addDbManager"})
	public @ResponseBody ResponseResult addDbManager(@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "vconnid", required = true) String vconnid
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("selectItem", selectItem);
		paramMap.put("vconnid", vconnid);
		paramMap.put("mode", mode);
		
		return managerMgmtServiceImpl.updateDbManager(paramMap);
	}
	
	@RequestMapping({"/dbManagerList"})
	public @ResponseBody ResponseResult dbManagerList(@RequestParam(value = "vconnid", required = true) String vconnid) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("vconnid", vconnid);
		
		return managerMgmtServiceImpl.selectDatabaseManager(paramMap);
	}
}
