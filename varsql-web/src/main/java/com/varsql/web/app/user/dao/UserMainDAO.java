package com.varsql.web.app.user.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.app.user.beans.MemoInfo;
import com.varsql.web.common.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;

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
	/**
	 *
	 * @Method Name  : selectSearchUserList
	 * @Method 설명 : 사용자 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 31.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public List selectSearchUserList(ParamMap paramMap) {
		return getSqlSession().selectList("userMapper.selectSearchUserList", paramMap );
	}

	/**
	 *
	 * @Method Name  : selectSendMemoUser
	 * @Method 설명 : 보낸 사용자 구하기
	 * @작성자   : ytkim
	 * @작성일   : 2019. 5. 2.
	 * @변경이력  :
	 * @param memoInfo
	 * @return
	 */
	public String selectSendMemoUser(MemoInfo memoInfo) {
		return getSqlSession().selectOne("userMapper.selectSendMemoUser", memoInfo);
	}

	public int insertSendMemoInfo(MemoInfo memoInfo) {
		return getSqlSession().insert("userMapper.insertSendMemoInfo", memoInfo );
	}
	public int insertSendUserInfo(MemoInfo memoInfo) {
		SqlSession batch = getSqlSession(true);
		String [] recvArr = memoInfo.getRecvId().split(";;");

		try{
			for (int i = 0; i < recvArr.length; i++) {
				memoInfo.setRecvId(recvArr[i]);
				batch.insert("userMapper.insertSendUserInfo", memoInfo);
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

}
