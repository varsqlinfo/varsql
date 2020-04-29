package com.varsql.web.app.manager.service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.auth.AuthorityType;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.web.app.manager.dao.ManagerDAO;
import com.varsql.web.app.user.beans.PasswordForm;
import com.varsql.web.app.user.dao.UserPreferencesDAO;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.dao.CommonDAO;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.spec.UserSpec;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.encryption.PasswordUtil;

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
	ManagerDAO manageDAO;

	@Autowired
	UserPreferencesDAO userPreferencesDAO;

	@Autowired
	CommonDAO commonDAO;
	
	@Autowired
	private UserMgmtRepository userMgmtRepository;
	
	@Resource(name="varsqlPasswordEncoder")
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

		return VarsqlUtils.getResponseResult(result, searchParameter, domainMapper, UserResponseDTO.class);
	}

	/**
	 *
	 * @Method Name  : updateAccept
	 * @Method 설명 : 사용자 수락 거부 .
	 * @작성자   : ytkim
	 * @작성일   : 2017. 12. 1.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult updateAccept(String acceptyn, String selectItem) {
		ResponseResult result = new ResponseResult();
		String[] viewidArr = StringUtil.split(selectItem,",");
		AuthorityType role = "Y".equals(acceptyn)?AuthorityType.USER:AuthorityType.GUEST;
		
		List<UserEntity> users= userMgmtRepository.findByViewidIn(Arrays.asList(viewidArr)).stream().map(item -> {
			item.setUserRole(role.name());
			item.setAcceptYn("Y".equals(acceptyn)?true:false);
			return item; 
		}).collect(Collectors.toList());
		
		userMgmtRepository.saveAll(users);
		
		result.setItemOne(1);

		return result;
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
	public ResponseResult initPassword(PasswordForm passwordForm) throws EncryptDecryptException {
		ResponseResult result = new ResponseResult();

		String passwordInfo = PasswordUtil.createPassword(Configuration.getInstance().passwordType(), Configuration.getInstance().passwordInitSize());

		passwordForm.setUpw(passwordEncoder.encode(passwordInfo));
		result.setResultCode(userPreferencesDAO.updatePasswordInfo(passwordForm));
		result.setItemOne(passwordInfo);

		return result;
	}

	/**
	 *
	 * @Method Name  : userDetail
	 * @Method 설명 : 사용자 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 29.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult userDetail(ParamMap param) {
		ResponseResult result = new ResponseResult();

		result.setItemOne(manageDAO.selectUserDetail(param.getString("viewid")));
		result.setItemList(manageDAO.selectUserDbInfo(param));
		result.addCustoms("isAdmin",SecurityUtil.isAdmin());
		result.addCustoms("dbGroup",manageDAO.selectUserDbGroup(param));

		return result;
	}

	/**
	 *
	 * @Method Name  : removeAuth
	 * @Method 설명 : 권한 지우기
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30.
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult removeAuth(ParamMap param) {
		ResponseResult result = new ResponseResult();

		boolean chkFlag = false;
		if(SecurityUtil.isAdmin()){
			chkFlag =true;
		}else{
			int cnt = manageDAO.selectDbManagerCheck(param);
			if(cnt > 0){
				chkFlag =true;
			}
		}

		if(chkFlag){
			if("block".equals(param.getString("mode"))) {
				result.setItemOne(manageDAO.inserDbBlockUser(param));
			}else {
				result.setItemOne(manageDAO.deleteDbBlockUser(param));
			}
		}else {
			result.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
		}

		return result;
	}

	/**
	 *
	 * @Method Name  : updateBlockYn
	 * @Method 설명 : 차단  y & n
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 7.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult updateBlockYn(DataCommonVO paramMap) {
		ResponseResult result = new ResponseResult();

		boolean chkFlag = false;
		if(SecurityUtil.isAdmin()){
			chkFlag =true;
		}else if(SecurityUtil.isManager()){
			if(commonDAO.selectManagerCheck(paramMap) > 0) {
				chkFlag =true;
			}
		}

		if(chkFlag){
			result.setItemOne(manageDAO.updateBlockYn(paramMap));
		}else {
			result.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
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
	public ResponseResult removeDbGroup(ParamMap param) {
		ResponseResult result = new ResponseResult();

		boolean chkFlag = false;
		if(SecurityUtil.isAdmin() || SecurityUtil.isManager()){
			chkFlag =true;
		}

		if(chkFlag){
			result.setItemOne(manageDAO.deleteUserDbGroup(param));
		}else {
			result.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
		}

		return result;
	}
}