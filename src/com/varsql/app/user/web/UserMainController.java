package com.varsql.app.user.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.app.common.enums.ViewPage;
import com.varsql.app.common.web.AbstractController;
import com.varsql.app.user.beans.MemoInfo;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.user.service.UserMainServiceImpl;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.beans.DatabaseInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;



/**
 * 
 * 
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

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UserMainController.class);
	
	@Autowired
	UserMainServiceImpl userMainServiceImpl;
	
	@RequestMapping({"","/","/main"})
	public ModelAndView mainpage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put("role", SecurityUtil.loginRole(req));
		paramMap.put("uid", SecurityUtil.loginId(req));
		
		ModelMap model = mav.getModelMap();
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
		SecurityUtil.reloadUserDatabaseInfo();
		model.addAttribute("dblist", SecurityUtil.loginInfo(req).getDatabaseInfo().values());
		
		return getModelAndView("/userMain", ViewPage.USER, model);
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
	@RequestMapping({"/searchUserList"})
	public @ResponseBody ResponseResult searchUserList(HttpServletRequest req, HttpServletResponse response) throws Exception {	
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.selectSearchUserList(paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : sendMemo
	 * @Method 설명 : 메시지 보내기
	 * @작성자   : ytkim
	 * @작성일   : 2019. 5. 2. 
	 * @변경이력  :
	 * @param memoInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sendMemo", "/resendMemo"})
	public @ResponseBody ResponseResult sendMemo(@Valid MemoInfo memoInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController sendMemo check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			
			String requestURI = req.getRequestURI();
			
			memoInfo.setRegId(SecurityUtil.loginId(req));
			resultObject = userMainServiceImpl.insertSendMemoInfo(memoInfo, requestURI.indexOf("resendMemo") > -1 ? true : false); 
		}
		
		return  resultObject;
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
	@RequestMapping({"/message"})
	public @ResponseBody ResponseResult message(HttpServletRequest req, HttpServletResponse response) throws Exception {
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.selectMessageInfo(paramMap);
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
	@RequestMapping({"/updMsgViewDt"})
	public @ResponseBody ResponseResult updMsgViewDt(HttpServletRequest req, HttpServletResponse response) throws Exception {
		
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));
		
		return userMainServiceImpl.updateMemoViewDate(paramMap);
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
	@RequestMapping(value="/connectionInfo")
	public @ResponseBody ResponseResult connectionInfo(HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		
		SecurityUtil.reloadUserDatabaseInfo();
		
		Collection<DatabaseInfo> dataBaseInfo = SecurityUtil.loginInfo(req).getDatabaseInfo().values();
		
		List databaseList =new ArrayList();
		dataBaseInfo.forEach(item -> {
			Map addMap = new HashMap();
			
			addMap.put("uuid", item.getConnUUID());
			addMap.put("type", item.getType());
			addMap.put("name", item.getName());
			databaseList.add(addMap);
		});
		
		resultObject.setItemList(databaseList);
		
		return resultObject; 
	}
}
