package com.varsql.app.user.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResultConstants;
import com.varsql.app.user.beans.MemoInfo;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.user.dao.UserMainDAO;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
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
	 * @Method Name  : insertSendMemoInfo
	 * @Method 설명 : sql 보내기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional
	public ResponseResult insertSendMemoInfo(MemoInfo memoInfo, boolean resendFlag) {
		ResponseResult result = new ResponseResult();
		
		if(resendFlag) {
			memoInfo.setMemoCont(memoInfo.getReMemoCont());
			memoInfo.setMemoTitle("[re]" + memoInfo.getMemoTitle());
			memoInfo.setRecvId(userMainDAO.selectSendMemoUser(memoInfo));
			memoInfo.setParentMemoId(memoInfo.getMemoId());
		}
		
		memoInfo.setMemoId(VarsqlUtil.generateUUID());
		
		userMainDAO.insertSendMemoInfo(memoInfo);
		result.setItemOne(userMainDAO.insertSendUserInfo(memoInfo));
		
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
	 * @param req 
	 * @param res 
	 * @return
	 */
	public boolean updateUserInfo(UserForm userForm, HttpServletRequest req, HttpServletResponse res) {
		boolean flag = userMainDAO.updateUserInfo(userForm)> 0;
		
		if(flag) {
			// 언어 변경시 처리.
			Locale userLocale= LocaleConstants.parseLocaleString(userForm.getLang());
			
			if(userLocale != null  && !userLocale.equals(SecurityUtil.loginInfo().getUserLocale())) {
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
				if (localeResolver == null) {
					throw new IllegalStateException("No LocaleResolver found.");
				}
				
				if(localeResolver.resolveLocale(req) != userLocale) {
					localeResolver.setLocale(req, res, userLocale);
				}
				
				SecurityUtil.loginInfo().setUserLocale(userLocale);
			}
		}
		
		return flag;
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
	public ResponseResult selectUserMsgReply(ParamMap ParamMap) {
		ResponseResult result = new ResponseResult();
		result.setItemList(userMainDAO.selectUserMsgReply(ParamMap));
		return result;
	}

	public ResponseResult deleteUserMsg(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		
		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");
		
		result.setItemOne(userMainDAO.deleteUserMsg(viewidArr,paramMap));
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : selectQna
	 * @Method 설명 : qna list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectQna(SearchParameter searchParameter) {
		
		ResponseResult result = new ResponseResult();
		
		int totalcnt = userMainDAO.selectQnaTotalCnt(searchParameter);
		
		if(totalcnt > 0){
			result.setItemList(userMainDAO.selectQna(searchParameter));
		}else{
			result.setItemList(null);
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));
		
		return result;
	}
	/**
	 * 
	 * @Method Name  : insertQnaInfo
	 * @Method 설명 : qna 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult saveQnaInfo(QnAInfo qnaInfo, boolean insFlag) {
		
		ResponseResult result = new ResponseResult();
		
		if(insFlag){
			qnaInfo.setQnaid(VarsqlUtil.generateUUID());
			result.setItemOne(userMainDAO.insertQnaInfo(qnaInfo) );
		}else{
			result.setItemOne(userMainDAO.updateQnaInfo(qnaInfo) );
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : deleteQnaInfo
	 * @Method 설명 : qna 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult deleteQnaInfo(QnAInfo qnaInfo) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(userMainDAO.deleteQnaInfo(qnaInfo));
		return result;
	}
	

	/**
	 * 
	 * @Method Name  : selectDetailQna
	 * @Method 설명 :  q&a 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult selectDetailQna(QnAInfo qnaInfo) {
		ResponseResult result = new ResponseResult();
		result.setItemOne( userMainDAO.selectDetailQna(qnaInfo));
		return result;
	}
}