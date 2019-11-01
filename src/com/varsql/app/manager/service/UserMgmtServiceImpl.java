package com.varsql.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.CommonDAO;
import com.varsql.app.manager.dao.ManagerDAO;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.dao.UserMainDAO;
import com.varsql.app.user.dao.UserPreferencesDAO;
import com.varsql.app.util.VarsqlUtils;
import com.varsql.core.auth.Authority;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.encryption.PasswordUtil;
import com.vartech.common.utils.PagingUtil;

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
public class UserMgmtServiceImpl{
	
	@Autowired
	ManagerDAO manageDAO;
	
	@Autowired
	UserPreferencesDAO userPreferencesDAO;
	
	@Autowired
	CommonDAO commonDAO;
	
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
		
		ResponseResult result = new ResponseResult();
		
		searchParameter.addCustomParam("isAdmin",SecurityUtil.isAdmin());
		
		int totalcnt = manageDAO.selectUserTotalcnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(manageDAO.selectUserList(searchParameter));
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
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
	public ResponseResult updateAccept(DataCommonVO paramMap) {
		ResponseResult result = new ResponseResult();
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		String role = paramMap.getString("acceptyn").equals("Y")?Authority.USER.name():Authority.GUEST.name();
		
		paramMap.put("role", role);
		
		result.setItemOne(manageDAO.updateAccept(viewidArr, paramMap));
		
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
		
		passwordForm.setUpw(EncryptionFactory.getInstance().encrypt(passwordInfo));
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