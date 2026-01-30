package com.varsql.web.app.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.user.service.UserNoteServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.MessageType;
import com.varsql.web.dto.user.NoteRequestDTO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

/**
 * UserNoteController
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/user/note")
public class UserNoteController extends AbstractController {

    private final Logger logger = LoggerFactory.getLogger(UserNoteController.class);

    private final UserNoteServiceImpl userNoteServiceImpl;
    
    
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
	@RequestMapping(value={"/send", "/resend"}, method = RequestMethod.POST)
	public @ResponseBody ResponseResult sendNote(@Valid NoteRequestDTO noteInfo, BindingResult result, HttpServletRequest req) throws Exception {
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController sendNote check {}", errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		
		noteInfo.setNoteType(MessageType.NOTE);

		return userNoteServiceImpl.insertSendNoteInfo(noteInfo, req.getRequestURI().indexOf("resend") > -1 ? true : false);
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
	public @ResponseBody ResponseResult message(@RequestParam(value = "viewMode" , required = true)  String viewMode, HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		return userNoteServiceImpl.selectMessageInfo(viewMode, searchParameter);
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
		return userNoteServiceImpl.updateNoteViewDate(noteId);
	}
    
	/**
	 * @Method Name  : listMsg
	 * @Method 설명 : 메시지 목록보기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public @ResponseBody ResponseResult list(@RequestParam(value = "viewMode" , required = true)  String viewMode, HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return userNoteServiceImpl.selectUserMsg(viewMode, searchParameter);
	}

	@RequestMapping(value="/msgReplyList", method = RequestMethod.POST)
	public @ResponseBody ResponseResult msgReplyList(NoteRequestDTO noteInfo) throws Exception {
		return userNoteServiceImpl.selectUserMsgReply(noteInfo);
	}

	/**
	 * @Method Name  : deleteMsg
	 * @Method 설명 : 메시지 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteMsg", method = RequestMethod.POST)
	public @ResponseBody ResponseResult deleteMsg(@RequestParam(value = "viewMode" , required = true)  String viewMode
			,@RequestParam(value = "selectItem" , required = true)  String selectItem) throws Exception {

		return userNoteServiceImpl.deleteUserMsg( viewMode,  selectItem);
	}
}
