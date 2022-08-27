package com.varsql.web.app.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.web.app.database.service.DatabaseServiceImpl;
import com.varsql.web.app.user.service.UserMainServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.user.NoteRequestDTO;
import com.varsql.web.util.DatabaseUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechUtils;


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
public class UserMainController extends AbstractController{

	private final Logger logger = LoggerFactory.getLogger(UserMainController.class);

	@Autowired
	private UserMainServiceImpl userMainServiceImpl;

	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;

	@RequestMapping(value={"","/","/main"}, method = RequestMethod.GET)
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		DatabaseUtils.reloadUserDatabaseInfo();
		model.addAttribute("dblist", SecurityUtil.loginInfo(req).getDatabaseInfo());

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
	 * @Method Name  : sendNote
	 * @Method 설명 : 쪽지 보내기
	 * @작성자   : ytkim
	 * @작성일   : 2019. 5. 2.
	 * @변경이력  :
	 * @param noteInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/sendNote", "/resendNote"}, method = RequestMethod.POST)
	public @ResponseBody ResponseResult sendNote(@Valid NoteRequestDTO noteInfo, BindingResult result, HttpServletRequest req) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController sendNote check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}

		return userMainServiceImpl.insertSendNoteInfo(noteInfo, req.getRequestURI().indexOf("resendNote") > -1 ? true : false);
	}

	/**
	 *
	 * @Method Name  : message
	 * @Method 설명 : 사용자 메모 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/message"}, method = RequestMethod.POST)
	public @ResponseBody ResponseResult message(@RequestParam(value = "messageType" , required = true)  String messageType, HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		
		return userMainServiceImpl.selectMessageInfo(messageType, searchParameter);
	}

	/**
	 *
	 * @Method Name  : updMsgViewDt
	 * @Method 설명 : 메시지 확인 날짜 업데이트
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param req
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/updMsgViewDt"}, method = RequestMethod.POST)
	public @ResponseBody ResponseResult updMsgViewDt(@RequestParam(value = "noteId" , required = true) String noteId) throws Exception {
		return userMainServiceImpl.updateNoteViewDate(noteId);
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

		DatabaseUtils.reloadUserDatabaseInfo();
		
		List<HashMap<String,String>> databaseList =new ArrayList<>();
		for(Entry<String, DatabaseInfo> entry :  SecurityUtil.loginInfo(req).getDatabaseInfo().entrySet()) {
			HashMap<String,String> addMap = new HashMap<>();
			
			DatabaseInfo item = entry.getValue();

			addMap.put("uuid", entry.getKey());
			addMap.put("type", item.getType());
			addMap.put("name", item.getName());
			databaseList.add(addMap);
		}

		resultObject.setItemList(databaseList);

		return resultObject;
	}
}
