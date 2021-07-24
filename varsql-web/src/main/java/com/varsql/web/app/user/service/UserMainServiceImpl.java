package com.varsql.web.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.websocket.service.WebSocketServiceImpl;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.MessageType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.WebSocketConstants;
import com.varsql.web.dto.user.NoteRequestDTO;
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.dto.websocket.MessageDTO;
import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.entity.app.NoteMappingUserEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.spec.NoteSpec;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.NoteEntityRepository;
import com.varsql.web.repository.user.NoteMappingUserEntityRepository;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.DefaultValueUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

@Service
public class UserMainServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(UserMainServiceImpl.class);

	@Autowired
	private UserMgmtRepository userMgmtRepository;

	@Autowired
	private NoteEntityRepository noteEntityRepository;
	
	@Autowired
	private WebSocketServiceImpl webSocketServiceImpl;

	@Autowired
	private NoteMappingUserEntityRepository noteMappingUserEntityRepository;

	/**
	 *
	 * @Method Name  : selectSearchUserList
	 * @Method 설명 : 사용자 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectSearchUserList(SearchParameter searchParameter) {
		List<UserEntity> result = userMgmtRepository.findAll(
			UserSpec.findUser(searchParameter.getKeyword())
		);

		return VarsqlUtils.getResponseResult(result, domainMapper, UserResponseDTO.class);
	}

	/**
	 *
	 * @Method Name  : insertSendNoteInfo
	 * @Method 설명 : 쪽지 정보 저장
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult insertSendNoteInfo(NoteRequestDTO noteInfo, boolean resendFlag) {

		logger.debug("insertSendNoteInfo resendFlag : {} , noteInfo : {} ", resendFlag, noteInfo );

		if(resendFlag) {
			NoteEntity orginNoteInfo= noteEntityRepository.findByNoteId(noteInfo.getNoteId());
			noteInfo.setNoteCont(noteInfo.getReNoteCont());
			noteInfo.setNoteTitle("[re]" + noteInfo.getNoteTitle());
			noteInfo.setParentNoteId(noteInfo.getNoteId());
			noteInfo.setRecvId(orginNoteInfo.getRegInfo().getViewid());
		}

		NoteEntity saveInfo = noteInfo.toEntity();
		saveInfo.setNoteId(resendFlag ? null : saveInfo.getNoteId());
		saveInfo = noteEntityRepository.save(saveInfo);

		String [] recvArr = noteInfo.getRecvId().split(";;");

		List<NoteMappingUserEntity>  recvList = new ArrayList<>();

		String sendId = SecurityUtil.userViewId();
		for (int i = 0; i < recvArr.length; i++) {
			recvList.add(NoteMappingUserEntity.builder()
					.noteId(saveInfo.getNoteId())
					.sendId(sendId)
					.recvId(recvArr[i])
					.build());
		}

		noteMappingUserEntityRepository.saveAll(recvList);
		
		webSocketServiceImpl.sendUserMessage(MessageDTO.builder()
				.type(MessageType.NOTE).message("recv").build(), recvArr);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @param messageType 
	 * @param searchParameter 
	 * @Method Name  : selectMessageInfo
	 * @Method 설명 : 쪽지 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectMessageInfo(String messageType, SearchParameter searchParameter) {
		if("recv".equals(messageType)) {
			return VarsqlUtils.getResponseResultItemList(noteEntityRepository.findAll(NoteSpec.recvMsg(SecurityUtil.userViewId() ,"" ), VarsqlUtils.convertSearchInfoToPage(searchParameter)).getContent());
		}else {
			return VarsqlUtils.getResponseResultItemList(noteEntityRepository.findAll(NoteSpec.userNoteList(SecurityUtil.userViewId())));
		}
	}

	/**
	 *
	 * @Method Name  : updateNoteViewDate
	 * @Method 설명 : 쪽지 확인일  업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateNoteViewDate(String noteId) {

		NoteMappingUserEntity noteMappingInfo = noteMappingUserEntityRepository.findByNoteIdAndRecvId(noteId, SecurityUtil.userViewId());

		if(noteMappingInfo != null) {
			noteMappingInfo.setViewDt(DefaultValueUtils.currentTimestamp());
			noteMappingInfo = noteMappingUserEntityRepository.save(noteMappingInfo);
		}

		return VarsqlUtils.getResponseResultItemOne(1);
	}
}