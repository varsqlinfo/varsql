package com.varsql.web.app.admin.managermgmt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.common.vo.DataCommonVO;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/admin/managerMgmt")
public class ManagerMgmtController{

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ManagerMgmtController.class);
	
	@Autowired
	ManagerMgmtService managerMgmtService; 
	
	@RequestMapping({"/userList"})
	public @ResponseBody String userList(@RequestParam(value = "searchval", required = false, defaultValue = "" )  String searchval
			,@RequestParam(value = "page", required = false, defaultValue = "1" )  int page
			,@RequestParam(value = "rows", required = false, defaultValue = "10" )  int rows
		) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("page", page);
		paramMap.put("rows", rows);
		paramMap.put("searchval", searchval);
		
		return managerMgmtService.selectRoleUserList(paramMap);
	}
	
	@RequestMapping({"/managerList"})
	public @ResponseBody String managerlist(@RequestParam(value = "searchval", required = false, defaultValue = "" )  String searchval
			,@RequestParam(value = "page", required = false, defaultValue = "1" )  int page
			,@RequestParam(value = "rows", required = false, defaultValue = "10" )  int rows
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("page", page);
		paramMap.put("rows", rows);
		paramMap.put("searchval", searchval);
		
		return managerMgmtService.selectRoleManagerList(paramMap);
	}
	
	@RequestMapping({"/managerRoleMgmt"})
	public @ResponseBody String managerRoleMgmt(@RequestParam(value = "mode", required = true, defaultValue = "del" )  String mode
			,@RequestParam(value = "viewid", required = true)  String viewid
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("mode", mode);
		paramMap.put("viewid", viewid);
		
		return managerMgmtService.updateManagerRole(paramMap);
	}
	
	@RequestMapping({"/addDbManager"})
	public @ResponseBody String addDbManager(@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = "vconid", required = true) String vconid
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("selectItem", selectItem);
		paramMap.put("vconid", vconid);
		
		return managerMgmtService.updateDbManager(paramMap);
	}
	
	@RequestMapping({"/dbManagerList"})
	public @ResponseBody String dbManagerList(@RequestParam(value = "vconid", required = true) String vconid) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("vconid", vconid);
		
		return managerMgmtService.selectDatabaseManager(paramMap);
	}
}
