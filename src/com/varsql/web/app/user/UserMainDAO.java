package com.varsql.web.app.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.app.user.beans.PasswordForm;
import com.varsql.web.app.user.beans.UserForm;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
import com.varsql.web.util.VarsqlUtil;
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
		return getSqlSession().selectList("userMapper.selectUserMsg", searchParameter);
	}

	public boolean deleteUserMsg(String[] viewidArr, ParamMap paramMap) {
		
		SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
            for(String id: viewidArr){
            	paramMap.put("memoId", id);
            	batchSqlSession.delete("userMapper.deleteUserMsg", paramMap);
            }
            batchSqlSession.commit();
            result = true; 
        }finally{
        	batchSqlSession.close();
        }
		return result;
	}
}
