package com.varsql.app.manager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.manager.dao.ManagerDAO;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.dao.UserMainDAO;
import com.varsql.core.auth.Authority;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
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
public class ManagerServiceImpl{
	
	@Autowired
	ManagerDAO manageDAO;
	
	@Autowired
	UserMainDAO userMainDAO;
	
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
		result.setResultCode(userMainDAO.updatePasswordInfo(passwordForm));
		result.setItemOne(passwordInfo);
		
		return result;
	}
	
}