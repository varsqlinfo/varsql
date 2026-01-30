package com.varsql.web.app.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.VarsqlDateUtils;
import com.varsql.web.app.websocket.service.WebSocketServiceImpl;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.MessageType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.SecurityConstants;
import com.varsql.web.dto.user.NoteRequestDTO;
import com.varsql.web.dto.user.NoteResponseDTO;
import com.varsql.web.dto.websocket.MessageDTO;
import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.entity.app.NoteMappingUserEntity;
import com.varsql.web.model.entity.user.RegInfoEntity;
import com.varsql.web.model.mapper.app.NoteMapper;
import com.varsql.web.repository.app.NoteEntityRepository;
import com.varsql.web.repository.app.NoteMappingUserEntityRepository;
import com.varsql.web.repository.spec.NoteSpec;
import com.varsql.web.util.DefaultValueUtils;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserNoteServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(UserNoteServiceImpl.class);

	private final NoteEntityRepository noteEntityRepository;

	private final NoteMappingUserEntityRepository noteMappingUserEntityRepository;

    private final WebSocketServiceImpl webSocketServiceImpl;
    
	
	/**
	 *
	 * @Method Name  : insertSendNoteInfo
	 * @Method 설명 : 쪽지 정보 저장
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param noteInfo
	 * @param resendFlag
	 * @return
	 */
	public ResponseResult insertSendNoteInfo(NoteRequestDTO noteInfo, boolean resendFlag) {
		return this.insertSendNoteInfo(noteInfo, noteInfo.getRecvId().split(";;"), resendFlag);
	}
	
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult insertSendNoteInfo(NoteRequestDTO noteInfo, String[] recvIds,boolean resendFlag) {

		logger.debug("insertSendNoteInfo resendFlag : {} , noteInfo : {} ", resendFlag, noteInfo );

		if(resendFlag) {
			NoteEntity orginNoteInfo= noteEntityRepository.findByNoteId(noteInfo.getNoteId());
			
			if(MessageType.BATCH.equals(orginNoteInfo.getNoteType())){
				ResponseResult resultObject = new ResponseResult();
				resultObject.setResultCode(RequestResultCode.DATA_NOT_VALID);
				resultObject.setMessage("Invalid message");
				return resultObject;
			}
			
			noteInfo.setNoteCont(noteInfo.getReNoteCont());
			noteInfo.setNoteTitle("[re]" + noteInfo.getNoteTitle());
			noteInfo.setParentNoteId(noteInfo.getNoteId());
			noteInfo.setRecvId(orginNoteInfo.getRegInfo().getViewid());
		}

		NoteEntity saveInfo = noteInfo.toEntity();
		saveInfo.setNoteId(resendFlag ? null : saveInfo.getNoteId());
		saveInfo = noteEntityRepository.save(saveInfo);

		List<NoteMappingUserEntity>  recvList = new ArrayList<>();
		
		String sendId;
		if(MessageType.NOTE.equals(noteInfo.getNoteType())) {
			sendId = SecurityUtil.userViewId();
		}else {
			sendId = SecurityConstants.SYSTEM_ID;
		}

		for (int i = 0; i < recvIds.length; i++) {
			recvList.add(NoteMappingUserEntity.builder()
					.noteId(saveInfo.getNoteId())
					.sendId(sendId)
					.recvId(recvIds[i])
					.build());
		}

		noteMappingUserEntityRepository.saveAll(recvList);

		webSocketServiceImpl.sendUserMessage(MessageDTO.builder()
				.type(noteInfo.getNoteType()).message("recv").recvIds(recvIds).build());

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : selectMessageInfo
	 * @Method 설명 : 쪽지 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param viewMode
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectMessageInfo(String viewMode, SearchParameter searchParameter) {
		List<NoteEntity> result =null;
		long totalCount = 0;
		if("recv".equals(viewMode)) {
			Page<NoteEntity> pageData = noteEntityRepository.findAll(NoteSpec.recvMsg(SecurityUtil.userViewId(), ""), VarsqlUtils.convertSearchInfoToPage(searchParameter));
			totalCount = pageData.getTotalElements();
			result = pageData.getContent();
		}else {
			result = noteEntityRepository.findAll(NoteSpec.userNoteList(SecurityUtil.userViewId()));
			totalCount = result.size();
		}
		
		List<NoteResponseDTO> noteList = new ArrayList<>();
		result.forEach(item ->{
			NoteResponseDTO noteResDto = NoteMapper.INSTANCE.toDto(item);
			noteList.add(noteResDto);
		});

		return VarsqlUtils.getResponseResult(noteList, totalCount, searchParameter);
	}

	/**
	 *
	 * @Method Name  : updateNoteViewDate
	 * @Method 설명 : 쪽지 확인일  업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param noteId
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
	/**
	 *
	 * @param viewMode
	 * @Method Name  : selectUserMsg
	 * @Method 설명 : 사용자 메시지 목록 [환경 설정]
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserMsg(String viewMode, SearchParameter searchParameter) {

		boolean isSend = "send".equals(viewMode); 
		Page<NoteEntity> result =null;
		if (isSend) {
			result = noteEntityRepository.findAll(NoteSpec.sendMsg(SecurityUtil.userViewId() ,searchParameter.getKeyword()) , VarsqlUtils.convertSearchInfoToPage(searchParameter));
		}else {
			result = noteEntityRepository.findAll(NoteSpec.recvMsg(SecurityUtil.userViewId() ,searchParameter.getKeyword()) , VarsqlUtils.convertSearchInfoToPage(searchParameter));
		}

		List<NoteResponseDTO> noteList = new ArrayList<>();
		result.getContent().forEach(item ->{

			NoteResponseDTO noteResDto = NoteMapper.INSTANCE.toDto(item);
			
			Set<NoteMappingUserEntity> recvList = item.getRecvList();
			
			if(recvList!=null && !recvList.isEmpty()) {
				if(isSend) {
					List<String> recvUsers = new ArrayList<>();
					recvList.stream().forEach(recvItem->{
						RegInfoEntity recvInfo = recvItem.getRecvInfo();
						if(recvInfo != null) {
							recvUsers.add(recvInfo.getUname()+"("+recvInfo.getUid()+")");
						}
					});
					noteResDto.setRecvUsers(recvUsers);
				}else {
					noteResDto.setViewDt(VarsqlDateUtils.timestampFormat(item.getRecvList().stream().findFirst().get().getViewDt()));
				}
			}

			noteList.add(noteResDto);
		});

		return VarsqlUtils.getResponseResult(noteList, result.getTotalElements() , searchParameter);
	}

	/**
	 *
	 * @Method Name  : selectUserMsgReply
	 * @Method 설명 : 답변 목록 구하기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 5. 2.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserMsgReply(NoteRequestDTO noteInfo) {
		return VarsqlUtils.getResponseResultItemList(noteEntityRepository.findAll(NoteSpec.findUserReply(noteInfo.getNoteId())));
	}

	/**
	 *
	 * @Method Name  : deleteUserMsg
	 * @Method 설명 : 메시지 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param messageType
	 * @param selectItem
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteUserMsg(String messageType, String selectItem) {

		String[] noteIdArr = StringUtils.split(selectItem,",");

    	if("send".equals(messageType)){ // 보낸 메시지 삭제시 만 처리.
    		noteEntityRepository.saveAllMsgDelYn(noteIdArr);
    		noteMappingUserEntityRepository.deleteSendMsgInfo(noteIdArr , SecurityUtil.userViewId());
    	}else {
    		noteMappingUserEntityRepository.deleteRecvMsgInfo(noteIdArr , SecurityUtil.userViewId());
    	}

		return VarsqlUtils.getResponseResultItemOne(1);
	}
}