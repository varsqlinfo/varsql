package com.varsql.web.app.manager.dbnuser;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.common.util.SecurityUtil;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/manager/dbnuser")
public class DbnUserController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(DbnUserController.class);
	
	@Autowired
	DbnUserServiceImpl dbnUserServiceImpl;
	
	@RequestMapping({"/dbList"})
	public @ResponseBody ResponseResult dbList(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		searchParameter.addCustomParam(UserConstants.ROLE, SecurityUtil.loginRole(req));
		searchParameter.addCustomParam(UserConstants.UID, SecurityUtil.loginId(req));
		return dbnUserServiceImpl.selectdbList(searchParameter);
	}
	
	@RequestMapping({"/dbnUserMappingList"})
	public @ResponseBody ResponseResult dbnUserMappingList(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true) String vconid) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		paramMap.put(VarsqlParamConstants.VCONNID, vconid);
		
		return dbnUserServiceImpl.selectDbUserMappingList(paramMap);
	}
	
	@RequestMapping({"/addDbUser"})
	public @ResponseBody ResponseResult addDbUser(@RequestParam(value = "selectItem", required = true)  String selectItem
			,@RequestParam(value = VarsqlParamConstants.VCONNID, required = true) String vconid
			,@RequestParam(value = "mode", required = true , defaultValue = "del") String mode
			, HttpServletRequest req
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("selectItem", selectItem);
		paramMap.put(VarsqlParamConstants.VCONNID, vconid);
		paramMap.put("upd_id", SecurityUtil.loginId(req));
		paramMap.put("mode", mode);
		
		return dbnUserServiceImpl.updateDbUser(paramMap);
	}
}
