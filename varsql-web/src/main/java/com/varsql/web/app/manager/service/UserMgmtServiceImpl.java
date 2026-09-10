package com.varsql.web.app.manager.service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.auth.AuthorityTypeImpl;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.websocket.service.WebSocketServiceImpl;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.MessageType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.websocket.MessageDTO;
import com.varsql.web.exception.VarsqlAccessDeniedException;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.db.DBBlockingUserEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.mapper.user.UserMapper;
import com.varsql.web.repository.db.DBBlockingUserEntityRepository;
import com.varsql.web.repository.db.DBGroupEntityRepository;
import com.varsql.web.repository.db.DBGroupMappingUserEntityRepository;
import com.varsql.web.repository.db.DBManagerEntityRepository;
import com.varsql.web.repository.spec.DBGroupSpec;
import com.varsql.web.repository.spec.DBManagerSpec;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.UserDBConnectionEntityRepository;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.crypto.password.PasswordUtil;
import com.vartech.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ManagerServiceImpl.java
* @DESC		: manager 서비스
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 12. 1. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserMgmtServiceImpl extends AbstractService{
	
	private final UserMgmtRepository userMgmtRepository;

	private final DBManagerEntityRepository dbManagerEntityRepository;

	private final DBBlockingUserEntityRepository dbBlockingUserEntityRepository;

	private final DBGroupMappingUserEntityRepository dbGroupMappingUserEntityRepository;

	private final DBGroupEntityRepository dbGroupEntityRepository;

	private final UserDBConnectionEntityRepository userDBConnectionEntityRepository;

	private final WebSocketServiceImpl webSocketServiceImpl;

	@Qualifier(ResourceConfigConstants.APP_PASSWORD_ENCODER)
	private final PasswordEncoder passwordEncoder;

	/**
	 *
	 * @Method Name  : selectUserList
	 * @Method 설명 : 사용자 목록 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserList(SearchParameter searchParameter) {

		Page<UserEntity> result = userMgmtRepository.findAll(
			UserSpec.likeUnameOrUid(SecurityUtil.isAdmin(), searchParameter.getKeyword())
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter, UserMapper.INSTANCE);
	}

	/**
	 *
	 * @Method Name  : updateAccept
	 * @Method 설명 : 사용자 수락 거부 .
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1.
	 * @변경이력  :
	 * @param acceptyn
	 * @param selectItem
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateAccept(String acceptyn, String selectItem) {
		String[] viewidArr = StringUtils.split(selectItem,",");
		AuthorityType role = "Y".equals(acceptyn)?AuthorityTypeImpl.USER:AuthorityTypeImpl.GUEST;

		log.info("user: {}, acceptyn: {}, view ids: {}", SecurityUtil.userViewId(), acceptyn, selectItem);

		List<UserEntity> targets = userMgmtRepository.findByViewidIn(Arrays.asList(viewidArr));

		// 요청자보다 상위 권한 계정이 대상에 섞여 있으면 한 건도 변경하지 않는다.
		checkModifiableTargets(targets, "updateAccept");

		List<UserEntity> users= targets.stream().map(item -> {
			item.setUserRole(role.getName());
			item.setAcceptYn("Y".equals(acceptyn)?true:false);
			return item;
		}).collect(Collectors.toList());

		userMgmtRepository.saveAll(users);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 * @method  : checkModifiableTargets
	 * @desc : 사용자 변경 대상이 모두 요청자보다 하위 권한인지 확인한다. 하나라도 아니면 예외.
	 * @param targets 변경 대상
	 * @param action 거부 로그에 남길 기능 이름
	 */
	private void checkModifiableTargets(List<UserEntity> targets, String action) {
		for(UserEntity target : targets) {
			if(!isModifiableTarget(target)) {
				log.warn("{} denied. request user: {}, target user: {}, target role: {}"
					, action, SecurityUtil.userViewId()
					, target == null ? null : target.getViewid()
					, target == null ? null : target.getUserRole());

				throw new VarsqlAccessDeniedException(RequestResultCode.FORBIDDEN.name());
			}
		}
	}

	/**
	 * @method  : isModifiableTarget
	 * @desc : 로그인한 사용자가 대상 계정을 변경할 수 있는지 확인한다.
	 *         요청자보다 우선순위가 같거나 높은 계정과 SYSTEM 계정은 대상이 될 수 없다.
	 *         (updateBlockYn 이 쓰던 검사를 다른 사용자 변경 기능에서도 공유한다)
	 * @param target 변경 대상
	 * @return 변경 가능 여부
	 */
	private boolean isModifiableTarget(UserEntity target) {
		if(target == null) {
			return false;
		}

		AuthorityType targetRole;
		try {
			targetRole = AuthorityTypeImpl.valueOf(target.getUserRole());
		}catch(IllegalArgumentException | NullPointerException e) {
			log.warn("unknown user role. viewid: {}, role: {}", target.getViewid(), target.getUserRole());
			return false;
		}

		// SYSTEM 은 priority 가 -999 라 우선순위 비교만으로는 걸러지지 않는다.
		if(AuthorityTypeImpl.SYSTEM.equals(targetRole)) {
			return false;
		}

		return SecurityUtil.loginInfo().getTopAuthority().getPriority() > targetRole.getPriority();
	}

	/**
	 *
	 * @Method Name  : initPassword
	 * @Method 설명 : 패스워드 초기화
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1.
	 * @변경이력  :
	 * @param PasswordForm
	 * @return
	 * @throws EncryptDecryptException
	 */
	public ResponseResult initPassword(String viewid) throws EncryptDecryptException {
		
		if(!Configuration.getInstance().getPasswordResetMode().equals(VarsqlConstants.PASSWORD_RESET_MODE.MANAGER)) {
			throw new VarsqlAppException(RequestResultCode.BAD_REQUEST.name());
		}
		
		log.info("init password user: {}, user id: {}", SecurityUtil.userViewId(), viewid);

		UserEntity entity= userMgmtRepository.findByViewid(viewid);

		// 요청자보다 상위 권한 계정의 비밀번호는 초기화할 수 없다.
		// (초기화된 비밀번호가 응답으로 돌아가므로 검사가 없으면 곧바로 계정 탈취가 된다)
		checkModifiableTargets(Arrays.asList(entity), "initPassword");

		String passwordInfo = PasswordUtil.createPassword(Configuration.getInstance().passwordType(), Configuration.getInstance().passwordInitSize());

		entity.setUpw(passwordInfo);
		entity = userMgmtRepository.save(entity);

		return VarsqlUtils.getResponseResultItemOne(passwordInfo);
	}

	/**
	 *
	 * @Method Name  : userDetail
	 * @Method 설명 : 사용자 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 29.
	 * @변경이력  :
	 * @param viewid
	 * @return
	 */
	public ResponseResult userDetail(String viewid) {
		ResponseResult result = new ResponseResult();

		result.setItemOne(UserMapper.INSTANCE.toDto(userMgmtRepository.findByViewid(viewid)));
		result.setList(userDBConnectionEntityRepository.userConnInfo(viewid));
		result.addCustomMapAttribute("dbGroup",dbGroupEntityRepository.findAll(DBGroupSpec.userGroupList(viewid)));

		return result;
	}

	/**
	 *
	 * @param param
	 * @Method Name  : removeAuth
	 * @Method 설명 : 권한 지우기
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult userDbBlock(String viewid, String vconnid, DataMap param) {
		ResponseResult result = new ResponseResult();

		boolean chkFlag = false;
		if(SecurityUtil.isAdmin()){
			chkFlag =true;
		}else{

			long cnt = dbManagerEntityRepository.count(DBManagerSpec.findVconnidManagerCheck(vconnid, SecurityUtil.userViewId()));
			if(cnt > 0){
				chkFlag =true;
			}
		}
		
		log.info("user db block user: {}, user id: {}, vconnid: {}", SecurityUtil.userViewId(), viewid, vconnid);

		if(chkFlag){
			if("block".equals(param.getString("mode"))) {
				dbBlockingUserEntityRepository.save(DBBlockingUserEntity.builder().viewid(viewid).vconnid(vconnid).build());

				webSocketServiceImpl.sendUserMessage(MessageDTO.builder().type(MessageType.USER_DB_BLOCK).message("block").recvIds(new String[] {viewid}).build().addItem("vconuid", UUIDUtil.vconnidUUID(viewid, vconnid)));
			}else {
				dbBlockingUserEntityRepository.deleteByVconnidAndViewid(vconnid, viewid);
			}
		}else {
			result.setResultCode(RequestResultCode.FORBIDDEN);
		}

		result.setItemOne(1);

		return result;
	}

	/**
	 *
	 * @Method Name  : updateBlockYn
	 * @Method 설명 : 차단  y & n
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 7.
	 * @변경이력  :
	 * @param viewid
	 * @param blockYn
	 * @return
	 */
	public ResponseResult updateBlockYn(String viewid, String blockYn) {
		ResponseResult result = new ResponseResult();

		boolean chkFlag = false;
		if(SecurityUtil.isAdmin()){
			chkFlag =true;
		}else if(SecurityUtil.isManager()){
			long cnt = userMgmtRepository.count(UserSpec.managerCheck(SecurityUtil.userViewId()));
			if(cnt > 0){
				chkFlag =true;
			}
		}
		
		log.info("user block user: {}, user id: {}, blockYn: {}", SecurityUtil.userViewId(), viewid, blockYn);

		if(chkFlag){
			UserEntity userInfo =userMgmtRepository.findByViewid(viewid);

			if(SecurityUtil.loginInfo().getTopAuthority().getPriority() > AuthorityTypeImpl.valueOf(userInfo.getUserRole()).getPriority()){
				boolean blockYFlag = "Y".equals(blockYn);
				if(blockYFlag) {
					userInfo.setBlockYn(true);
				}else {
					userInfo.setBlockYn(false);
				}

				userInfo = userMgmtRepository.save(userInfo);

				if(blockYFlag) {
					webSocketServiceImpl.sendUserMessage(MessageDTO.builder()
							.type(MessageType.USER_BLOCK).message("block").recvIds(new String[] {userInfo.getViewid()}).build());
				}
			}

			result.setItemOne(1);
		}else {
			result.setResultCode(RequestResultCode.FORBIDDEN);
		}

		return result;
	}

	/**
	 *
	 * @Method Name  : removeDbGroup
	 * @Method 설명 : 사용자 db 그룹 권한 제거.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 26.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult removeDbGroup(String groupId, String viewid) {
		ResponseResult result = new ResponseResult();

		boolean chkFlag = false;
		if(SecurityUtil.isAdmin() || SecurityUtil.isManager()){
			chkFlag =true;
		}
		
		log.info("remove user db group  user: {}, user id: {}, groupId: {}", SecurityUtil.userViewId(), viewid, groupId);

		if(chkFlag){
			dbGroupMappingUserEntityRepository.deleteByGroupIdAndViewid(groupId, viewid);
			result.setItemOne(1);
		}else {
			result.setResultCode(RequestResultCode.FORBIDDEN);
		}

		return result;
	}

}