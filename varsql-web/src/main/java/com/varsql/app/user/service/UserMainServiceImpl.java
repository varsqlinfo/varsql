package com.varsql.app.user.service;

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

import com.varsql.app.user.beans.MemoInfo;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.user.dao.UserMainDAO;
import com.varsql.app.util.VarsqlUtils;
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
	public ResponseResult selectSearchUserList(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		
		result.setItemList(userMainDAO.selectSearchUserList(paramMap));
		
		return result; 
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
		
		memoInfo.setMemoId(VarsqlUtils.generateUUID());
		
		userMainDAO.insertSendMemoInfo(memoInfo);
		result.setItemOne(userMainDAO.insertSendUserInfo(memoInfo));
		
		return result; 
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
	public ResponseResult selectMessageInfo(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		
		result.setItemList(userMainDAO.selectMessageInfo(paramMap));
			
		return result; 
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
	public ResponseResult updateMemoViewDate(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();
		
		result.setItemOne(userMainDAO.updateMemoViewDate(paramMap));
		
		return result;
	}
}