package com.varsql.web.app.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.app.user.beans.MemoInfo;
import com.varsql.web.app.user.dao.UserMainDAO;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;

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