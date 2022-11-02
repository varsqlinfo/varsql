package com.varsql.web.app.manager.service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.UUIDUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.websocket.service.WebSocketServiceImpl;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.MessageType;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.websocket.MessageDTO;
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
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.crypto.password.PasswordUtil;
import com.vartech.common.utils.StringUtils;

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
public class UserMgmtServiceImpl extends AbstractService{

	@Autowired
	private UserMgmtRepository userMgmtRepository;

	@Autowired
	private DBManagerEntityRepository dbManagerEntityRepository;

	@Autowired
	private DBBlockingUserEntityRepository dbBlockingUserEntityRepository;

	@Autowired
	private DBGroupMappingUserEntityRepository dbGroupMappingUserEntityRepository;

	@Autowired
	private DBGroupEntityRepository dbGroupEntityRepository;

	@Autowired
	private UserDBConnectionEntityRepository userDBConnectionEntityRepository;

	@Autowired
	private WebSocketServiceImpl webSocketServiceImpl;

	@Autowired
	@Qualifier(ResourceConfigConstants.APP_PASSWORD_ENCODER)
	private PasswordEncoder passwordEncoder;

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
		AuthorityType role = "Y".equals(acceptyn)?AuthorityType.USER:AuthorityType.GUEST;

		List<UserEntity> users= userMgmtRepository.findByViewidIn(Arrays.asList(viewidArr)).stream().map(item -> {
			item.setUserRole(role.name());
			item.setAcceptYn("Y".equals(acceptyn)?true:false);
			return item;
		}).collect(Collectors.toList());

		userMgmtRepository.saveAll(users);

		return VarsqlUtils.getResponseResultItemOne(1);
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
		
		String passwordInfo = PasswordUtil.createPassword(Configuration.getInstance().passwordType(), Configuration.getInstance().passwordInitSize());

		UserEntity entity= userMgmtRepository.findByViewid(viewid);
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

		if(chkFlag){
			if("block".equals(param.getString("mode"))) {
				dbBlockingUserEntityRepository.save(DBBlockingUserEntity.builder().viewid(viewid).vconnid(vconnid).build());

				webSocketServiceImpl.sendUserMessage(MessageDTO.builder().type(MessageType.USER_DB_BLOCK).message("block").build().addItem("vconuid", UUIDUtil.vconnidUUID(viewid, vconnid)), viewid);
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

		if(chkFlag){
			UserEntity userInfo =userMgmtRepository.findByViewid(viewid);

			if(SecurityUtil.loginInfo().getTopAuthority().getPriority() > AuthorityType.valueOf(userInfo.getUserRole()).getPriority()){
				boolean blockYFlag = "Y".equals(blockYn);
				if(blockYFlag) {
					userInfo.setBlockYn(true);
				}else {
					userInfo.setBlockYn(false);
				}

				userInfo = userMgmtRepository.save(userInfo);

				if(blockYFlag) {
					webSocketServiceImpl.sendUserMessage(MessageDTO.builder()
							.type(MessageType.USER_BLOCK).message("block").build(), userInfo.getViewid());
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

		if(chkFlag){
			dbGroupMappingUserEntityRepository.deleteByGroupIdAndViewid(groupId, viewid);
			result.setItemOne(1);
		}else {
			result.setResultCode(RequestResultCode.FORBIDDEN);
		}

		return result;
	}

}