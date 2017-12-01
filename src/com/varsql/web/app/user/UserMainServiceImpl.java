package com.varsql.web.app.user;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.db.encryption.EncryptionFactory;
import com.varsql.web.app.user.beans.PasswordForm;
import com.varsql.web.app.user.beans.UserForm;
import com.varsql.web.common.constants.ResultConstants;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.encryption.EncryptDecryptException;

@Service
public class UserMainServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(UserMainServiceImpl.class);
	@Autowired
	UserMainDAO userMainDAO;

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
	public Map selectSearchUserList(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT_ITEMS, userMainDAO.selectSearchUserList(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"selectSearchUserList", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	/**
	 * 
	 * @Method Name  : insertSendSqlInfo
	 * @Method 설명 : sql 보내기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public Map insertSendSqlInfo(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT, userMainDAO.insertSendSqlInfo(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"insertSendSqlInfo", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	
	public Map selectMessageInfo(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT_ITEMS, userMainDAO.selectMessageInfo(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
			
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"selectMessageInfo", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval; 
	}
	
	/**
	 * 
	 * @Method Name  : updateMemoViewDate
	 * @Method 설명 : 메모 보기 업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public Map updateMemoViewDate(ParamMap paramMap) {
		Map reval =  new HashMap();
		try{
			reval.put(ResultConstants.RESULT, userMainDAO.updateMemoViewDate(paramMap));
			reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.SUCCESS);
	    }catch(Exception e){
	    	reval.put(ResultConstants.CODE, ResultConstants.CODE_VAL.ERROR);
	    	logger.error(getClass().getName()+"updateMemoViewDate", e);
	    	reval.put("msg", e.getMessage());
	    }
		return reval;
	}
	
	/**
	 * 
	 * @Method Name  : updateUserInfo
	 * @Method 설명 : 사용자 정보 업데이트
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param userForm
	 * @return
	 */
	public boolean updateUserInfo(UserForm userForm) {
		return userMainDAO.updateUserInfo(userForm)> 0;
	}
	
	/**
	 * 
	 * @Method Name  : selectUserDetail
	 * @Method 설명 : 사용자 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param loginId
	 * @return
	 */
	public Map selectUserDetail(String loginId) {
		return userMainDAO.selectUserDetail(loginId);
	}
	
	/**
	 * 
	 * @Method Name  : updatePasswordInfo
	 * @Method 설명 : 비밀번호 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param passwordForm
	 * @return
	 * @throws EncryptDecryptException 
	 */
	public boolean updatePasswordInfo(PasswordForm passwordForm) throws EncryptDecryptException {
		passwordForm.setUpw(EncryptionFactory.getInstance().encrypt(passwordForm.getUpw()));
		return userMainDAO.updatePasswordInfo(passwordForm)> 0;
	}
	
	/**
	 * 
	 * @Method Name  : selectUserPasswordCheeck
	 * @Method 설명 : 패스워드 변경전 password check
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param passwordForm
	 * @return
	 */
	public int selectUserPasswordCheck(PasswordForm passwordForm) {
		return userMainDAO.selectUserPasswordCheck(passwordForm);
	}
}