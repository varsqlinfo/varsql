package com.varsql.app.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class ManagerDAO extends BaseDAO{
	
	
	public int selectUserTotalcnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectUserTotalcnt", searchParameter);
	}
	
	public List<Object> selectUserList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectUserList", searchParameter);
	}
	
	/**
	 * 
	 * @Method Name  : updateAccept
	 * @Method 설명 : 접근 여부 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 7. 
	 * @변경이력  :
	 * @param viewidArr
	 * @param paramMap
	 * @return
	 */
	public boolean updateAccept(String[] viewidArr, DataCommonVO paramMap){
        SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
            for(String id: viewidArr){
            	paramMap.put("viewid", id);
            	batchSqlSession.update("manageMapper.updateAccept", paramMap);
            }
            batchSqlSession.commit();
            result = true; 
        }finally{
        	batchSqlSession.close();
        }
		return result;
	}
	
	/**
	 * 
	 * @Method Name  : updateBlockYn
	 * @Method 설명 : 사용자 차단 여부 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 12. 7. 
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public int updateBlockYn(DataCommonVO paramMap) {
		return getSqlSession().update("manageMapper.updateBlockYn", paramMap);
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
		return getSqlSession().selectOne("manageMapper.selectUserDetail", loginId);
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
	public List selectUserDbInfo(ParamMap param) {
		return getSqlSession().selectList("manageMapper.selectUserDbInfo", param);
	}
	
	/**
	 * 
	 * @Method Name  : deleteUserDbAuth
	 * @Method 설명 : 사용자 db 권한 삭제. 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public int deleteUserDbAuth(ParamMap param) {
		return getSqlSession().delete("manageMapper.deleteUserDbAuth", param);
	}

	public int selectDbManagerCheck(ParamMap param) {
		return getSqlSession().selectOne("manageMapper.selectDbManagerCheck", param);
	}
}
