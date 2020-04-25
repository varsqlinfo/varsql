package com.varsql.web.app.user.service;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.varsql.web.app.user.beans.PasswordForm;
import com.varsql.web.app.user.beans.QnAInfo;
import com.varsql.web.app.user.beans.UserForm;
import com.varsql.web.app.user.dao.UserPreferencesDAO;
import com.varsql.web.constants.VarsqlErrorCode;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.utils.PagingUtil;

@Service
public class UserPreferencesServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesServiceImpl.class);

	@Autowired
	UserPreferencesDAO userPreferencesDAO;

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
		return userPreferencesDAO.selectUserDetail(loginId);
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
		boolean flag = userPreferencesDAO.updateUserInfo(userForm)> 0;

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
		String password = userPreferencesDAO.selectUserPasswordCheck(passwordForm);

		if(passwordForm.getCurrPw().equals(EncryptionFactory.getInstance().decrypt(password))){
			passwordForm.setUpw(EncryptionFactory.getInstance().encrypt(passwordForm.getUpw()));
			resultObject.setItemOne(userPreferencesDAO.updatePasswordInfo(passwordForm)> 0);
		}else{
			resultObject.setResultCode(VarsqlErrorCode.PASSWORD_NOT_VALID.code());
		}

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : selectUserMsg
	 * @Method 설명 : 사용자 메시지 목록 [환경 설정]
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserMsg(SearchParameter searchParameter) {
		ResponseResult result = new ResponseResult();

		int totalcnt = userPreferencesDAO.selectUserMsgTotalcnt(searchParameter);

		if(totalcnt > 0){
			result.setItemList(userPreferencesDAO.selectUserMsg(searchParameter));
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
		result.setItemList(userPreferencesDAO.selectUserMsgReply(ParamMap));
		return result;
	}

	/**
	 *
	 * @Method Name  : deleteUserMsg
	 * @Method 설명 : 메시지 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult deleteUserMsg(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();

		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");

		result.setItemOne(userPreferencesDAO.deleteUserMsg(viewidArr,paramMap));

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

		int totalcnt = userPreferencesDAO.selectQnaTotalCnt(searchParameter);

		if(totalcnt > 0){
			result.setItemList(userPreferencesDAO.selectQna(searchParameter));
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
			qnaInfo.setQnaid(VarsqlUtils.generateUUID());
			result.setItemOne(userPreferencesDAO.insertQnaInfo(qnaInfo) );
		}else{
			result.setItemOne(userPreferencesDAO.updateQnaInfo(qnaInfo) );
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
		result.setItemOne(userPreferencesDAO.deleteQnaInfo(qnaInfo));
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
		result.setItemOne( userPreferencesDAO.selectDetailQna(qnaInfo));
		return result;
	}
}