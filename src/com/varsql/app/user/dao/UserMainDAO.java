package com.varsql.app.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.ResourceConfigConstants;
import com.varsql.app.common.dao.BaseDAO;
import com.varsql.app.user.beans.PasswordForm;
import com.varsql.app.user.beans.UserForm;
import com.varsql.app.util.VarsqlUtil;
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
public class UserMainDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectdbListTotalCnt", paramMap);
	}
	
	public List<Object> selectdbList(DataCommonVO paramMap) {
		return getSqlSession().selectList("userMapper.selectdbList", paramMap);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("userMapper.updateQnaAnswerContent", paramMap);
	}
	
	public int selectDbUserMappingListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectDbUserMappingListTotalCnt", paramMap);
	}
	
	public List<Object>  selectDbUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("userMapper.selectDbUserMappingList", paramMap);
	}
	
	public List selectSearchUserList(ParamMap paramMap) {
		return getSqlSession().selectList("userMapper.selectSearchUserList", paramMap );
	}
	
	public int insertSendSqlInfo(ParamMap paramMap) {
		return getSqlSession().insert("userMapper.insertSendSqlInfo", paramMap ); 
	}
	public int insertSendUserInfo(ParamMap paramMap) {
		SqlSession batch = getSqlSession(true);
		String [] recvArr = paramMap.getString("recv_id").split(";;");
		
		try{
			for (int i = 0; i < recvArr.length; i++) {
				paramMap.put("recv_id", recvArr[i]);
				batch.insert("userMapper.insertSendUserInfo", paramMap );
			}
			batch.flushStatements();
			batch.commit(true);
		}finally{
			batch.close();
		}
		
		return 0; 
	}

	public List selectMessageInfo(ParamMap paramMap) {
		return getSqlSession().selectList("userMapper.selectMessageInfo", paramMap );
	}
	
	public int updateMemoViewDate(ParamMap paramMap) {
		return getSqlSession().update("userMapper.updateMemoViewDate", paramMap);
	}
	
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
		return getSqlSession().selectOne("userMapper.selectUserDetail", loginId);
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
		return getSqlSession().update("userMapper.updateUserInfo", userForm);
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
		return getSqlSession().update("userMapper.updatePasswordInfo", passwordForm);
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
		return getSqlSession().selectOne("userMapper.selectUserPasswordCheck", passwordForm);
	}

	public int selectUserMsgTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("userMapper.selectUserMsgTotalcnt", searchParameter);
	}

	public List selectUserMsg(SearchParameter searchParameter) {
		
		if("send".equals(searchParameter.getCustomParam().get("message_type"))){
			return getSqlSession().selectList("userMapper.selectUserSendMsg", searchParameter);
		}else{
			return getSqlSession().selectList("userMapper.selectUserRecvMsg", searchParameter);
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
            		batchSqlSession.update("userMapper.deleteMsgInfo", paramMap);
            	}
            	batchSqlSession.delete("userMapper.deleteMsgUser", paramMap);
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
	 * @Method Name  : selectUserDetailInfo
	 * @Method 설명 : 사용자 권한 있는 db 정보 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 29. 
	 * @변경이력  :
	 * @param userForm
	 * @return
	 */
	public List selectUserDbInfo(String viewid) {
		return getSqlSession().selectList("userMapper.selectUserDbInfo", viewid);
	}
}
