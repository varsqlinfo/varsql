package com.varsql.app.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResourceConfigConstants;
import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.user.beans.MemoInfo;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.QnAInfo;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.SearchParameter;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: UserMainDAO.java
* @DESC		: 사용자 정보 dao
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 29. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class UserPreferencesDAO extends BaseDAO{
	
	/**
	 * 
	 * @Method Name  : selectUserDetail
	 * @Method 설명 : 사용자 상세
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param loginId
	 * @return
	 */
	public Map selectUserDetail(String loginId) {
		return getSqlSession().selectOne("userPreferencesMapper.selectUserDetail", loginId);
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
	public int updateUserInfo(UserForm userForm) {
		return getSqlSession().update("userPreferencesMapper.updateUserInfo", userForm);
	}
	
	/**
	 * 
	 * @Method Name  : updatePasswordInfo
	 * @Method 설명 : 패스워드 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param passwordForm
	 * @return
	 */
	public int updatePasswordInfo(PasswordForm passwordForm) {
		return getSqlSession().update("userPreferencesMapper.updatePasswordInfo", passwordForm);
	}
	
	/**
	 * 
	 * @Method Name  : selectUserPasswordCheck
	 * @Method 설명 : password 변경 전 체크.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29. 
	 * @변경이력  :
	 * @param passwordForm
	 * @return
	 */
	public String selectUserPasswordCheck(PasswordForm passwordForm) {
		return getSqlSession().selectOne("userPreferencesMapper.selectUserPasswordCheck", passwordForm);
	}

	public int selectUserMsgTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("userPreferencesMapper.selectUserMsgTotalcnt", searchParameter);
	}

	public List selectUserMsg(SearchParameter searchParameter) {
		
		if("send".equals(searchParameter.getCustomParam().get("message_type"))){
			return getSqlSession().selectList("userPreferencesMapper.selectUserSendMsg", searchParameter);
		}else{
			return getSqlSession().selectList("userPreferencesMapper.selectUserRecvMsg", searchParameter);
		}
		
	}
	
	/**
	 * 
	 * @Method Name  : deleteUserMsg
	 * @Method 설명 : 메시지 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 12. 
	 * @변경이력  :
	 * @param viewidArr
	 * @param paramMap
	 * @return
	 */
	@Transactional(transactionManager=ResourceConfigConstants.APP_TRANSMANAGER , rollbackFor=Exception.class)
	public boolean deleteUserMsg(String[] viewidArr, ParamMap paramMap) {
		
		SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean rollbackFlag = true;
        
        try {
        	boolean sendFlag ="send".equals(paramMap.getString("message_type"));  
            for(String id: viewidArr){
            	paramMap.put("memoId", id);
            	if(sendFlag){ // 보낸 메시지 삭제시 만 처리. 
            		batchSqlSession.update("userPreferencesMapper.deleteMsgInfo", paramMap);
            	}
            	batchSqlSession.delete("userPreferencesMapper.deleteMsgUser", paramMap);
            }
            batchSqlSession.flushStatements();
            rollbackFlag = false; 
        }finally{
        	if(rollbackFlag){
        		batchSqlSession.rollback();
        	}
        	batchSqlSession.close();
        }
		return !rollbackFlag;
	}
	
	/**
	 * 
	 * @Method Name  : selectQnaTotalCnt
	 * @Method 설명 : qna total count
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int selectQnaTotalCnt(SearchParameter paramMap) {
		return getSqlSession().selectOne("userPreferencesMapper.selectQnaTotalCnt", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : selectQna
	 * @Method 설명 : q&a list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public List selectQna(SearchParameter paramMap) {
		return getSqlSession().selectList("userPreferencesMapper.selectQna", paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : selectDetailQna
	 * @Method 설명 : qna 상세
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public Object selectDetailQna(QnAInfo qnaInfo) {
		return getSqlSession().selectOne("userPreferencesMapper.selectDetailQna", qnaInfo);
	}
	
	/**
	 * 
	 * @Method Name  : insertQnaInfo
	 * @Method 설명 : qna 등록
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int insertQnaInfo(QnAInfo qnaInfo){
		return getSqlSession().insert("userPreferencesMapper.insertQnaInfo", qnaInfo );
	}
	
	/**
	 * 
	 * @Method Name  : deleteQnaInfo
	 * @Method 설명 : qna 삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int deleteQnaInfo(QnAInfo qnaInfo){
		return getSqlSession().delete("userPreferencesMapper.deleteQnaInfo", qnaInfo );
	}
	
	/**
	 * 
	 * @Method Name  : updateQnaInfo
	 * @Method 설명 : qna 업데이트
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int updateQnaInfo(QnAInfo qnaInfo){
		return getSqlSession().update("userPreferencesMapper.updateQnaInfo", qnaInfo );
	}
	
	/**
	 * 
	 * @Method Name  : selectUserMsgReply
	 * @Method 설명 : 답장 목록 구하기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 5. 2. 
	 * @변경이력  :
	 * @param memoInfo
	 * @return
	 */
	public List selectUserMsgReply(ParamMap param) {
		return getSqlSession().selectList("userPreferencesMapper.selectUserMsgReply", param);
	}
	
	/**
	 * 
	 * @Method Name  : selectUserSqlFileTotalCnt
	 * @Method 설명 : sql file count
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public int selectUserSqlFileTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("userPreferencesMapper.selectUserSqlFileTotalCnt", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : selectUserSqlFileList
	 * @Method 설명 : sql file list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31. 
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public List selectUserSqlFileList(SearchParameter searchParameter) {
		return getSqlSession().selectList("userPreferencesMapper.selectUserSqlFileList", searchParameter);
	}

	public Object selectSqlFiledetail(ParamMap param) {
		return getSqlSession().selectOne("userPreferencesMapper.selectSqlFiledetail", param);
	}
}
