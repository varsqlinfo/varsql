package com.varsql.web.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.user.dao.UserMainDAO;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.user.NoteRequestDTO;
import com.varsql.web.dto.user.UserResponseDTO;
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
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;

@Service
public class UserMainServiceImpl extends AbstractService{
	private static final Logger logger = LoggerFactory.getLogger(UserMainServiceImpl.class);
	@Autowired
	UserMainDAO userMainDAO;
	
	@Autowired
	private UserMgmtRepository userMgmtRepository;
	
	@Autowired
	private NoteEntityRepository noteEntityRepository;
	
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
	 * @Method Name  : insertSendMemoInfo
	 * @Method 설명 : sql 보내기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult insertSendMemoInfo(NoteRequestDTO noteInfo, boolean resendFlag) {
		
		if(resendFlag) {
			noteInfo.setNoteCont(noteInfo.getReNoteCont());
			noteInfo.setNoteTitle("[re]" + noteInfo.getNoteTitle());
			noteInfo.setRecvId(userMainDAO.selectSendMemoUser(noteInfo));
			noteInfo.setParentNoteId(noteInfo.getNoteId());
		}
		
		NoteEntity saveInfo = noteInfo.toEntity();
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
		
		return VarsqlUtils.getResponseResultItemOne(1); 
	}
	
	/**
	 * 
	 * @Method Name  : selectMessageInfo
	 * @Method 설명 : 메시지 목록. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectMessageInfo() {
		return VarsqlUtils.getResponseResultItemList(noteEntityRepository.findAll(NoteSpec.userNoteList(SecurityUtil.userViewId()))); 
	}
	
	/**
	 * 
	 * @Method Name  : updateMemoViewDate
	 * @Method 설명 : 메모 확인일  업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateNoteViewDate(String noteId) {
		
		NoteMappingUserEntity noteMappingInfo = noteMappingUserEntityRepository.findByNoteId(noteId);
		
		noteMappingInfo.setViewDt(DefaultValueUtils.currentTimestamp());
		
		noteMappingUserEntityRepository.save(noteMappingInfo);
		
		return VarsqlUtils.getResponseResultItemOne(1); 
	}
}