package com.varsql.web.app.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.app.database.service.DatabaseServiceImpl;
import com.varsql.web.app.user.service.UserMainServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.common.service.UserCommonService;
import com.varsql.web.constants.VIEW_PAGE;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechUtils;

import lombok.RequiredArgsConstructor;


/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserMainController.java
* @DESC		: 사용자 관리 컨트롤러.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 28. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMainController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(UserMainController.class);

	private final UserMainServiceImpl userMainServiceImpl;

	private final DatabaseServiceImpl databaseServiceImpl;
	
	private final UserCommonService userCommonService;

	@RequestMapping(value={"","/","/main"}, method = RequestMethod.GET)
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		
		model.addAttribute("dblist", userCommonService.reloadDatabaseList());

		// tab 정보
		model.addAttribute("conTabInfo", VartechUtils.objectToJsonString(databaseServiceImpl.findTabInfo()));

		return getModelAndView("/userMain", VIEW_PAGE.USER, model);
	}

	/**
	 *
	 * @Method Name  : searchUserList
	 * @Method 설명 : 사용자 검색 .
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/searchUserList"}, method = RequestMethod.POST)
	public @ResponseBody ResponseResult searchUserList(HttpServletRequest req, HttpServletResponse response) throws Exception {

		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return userMainServiceImpl.selectSearchUserList(searchParameter);
	}

	/**
	 *
	 * @Method Name  : connectionInfo
	 * @Method 설명 : 사용자 권한 있는 db 정보.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/connectionInfo", method = RequestMethod.POST)
	public @ResponseBody ResponseResult connectionInfo(HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();

		List<HashMap<String,String>> databaseList =new ArrayList<>();
		for(Entry<String, DatabaseInfo> entry :  userCommonService.reloadDatabaseList().entrySet()) {
			HashMap<String,String> addMap = new HashMap<>();
			
			DatabaseInfo item = entry.getValue();

			addMap.put("uuid", entry.getKey());
			addMap.put("type", item.getType());
			addMap.put("name", item.getName());
			databaseList.add(addMap);
		}

		resultObject.setList(databaseList);

		return resultObject;
	}
}
