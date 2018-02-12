package com.varsql.app.user.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.app.common.constants.ResultConstants;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.user.dao.UserMainDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.utils.PagingUtil;

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
	@Transactional
	public ResponseResult insertSendSqlInfo(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		paramMap.put("memo_id", VarsqlUtil.generateUUID());
		userMainDAO.insertSendSqlInfo(paramMap);
		
		result.setItemOne(userMainDAO.insertSendUserInfo(paramMap));
		
		return result; 
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
	 * @param resultObject 
	 * @return
	 * @throws EncryptDecryptException 
	 */
	public ResponseResult updatePasswordInfo(PasswordForm passwordForm, ResponseResult resultObject) throws EncryptDecryptException {
		String password = userMainDAO.selectUserPasswordCheck(passwordForm);
		
		if(passwordForm.getCurrPw().equals(EncryptionFactory.getInstance().decrypt(password))){
			passwordForm.setUpw(EncryptionFactory.getInstance().encrypt(passwordForm.getUpw()));
			resultObject.setItemOne(userMainDAO.updatePasswordInfo(passwordForm)> 0);
		}else{
			resultObject.setResultCode(ResultConst.CODE.FORBIDDEN.toInt());
		}
		
		return resultObject;
	}

	public ResponseResult selectUserMsg(SearchParameter searchParameter) {
		ResponseResult result = new ResponseResult();
		
		int totalcnt = userMainDAO.selectUserMsgTotalcnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(userMainDAO.selectUserMsg(searchParameter));
		}else{
			result.setItemList(null);
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
	}

	public ResponseResult deleteUserMsg(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		
		result.setItemOne(userMainDAO.deleteUserMsg(viewidArr,paramMap));
		
		return result;
	}
}