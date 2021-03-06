package com.varsql.web.app.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.dao.BaseDAO;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class ManagerDAO extends BaseDAO{
	
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
	 * @Method Name  : inserDbBlockUser
	 * @Method 설명 : 사용자 db 차단 
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 30. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public int inserDbBlockUser(ParamMap param) {
		return getSqlSession().delete("manageMapper.inserDbBlockUser", param);
	}
	
	/**
	 * 
	 * @Method Name  : deleteDbBlockUser
	 * @Method 설명 : 사용자  db 차단 해제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 20. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public int deleteDbBlockUser(ParamMap param) {
		return getSqlSession().delete("manageMapper.deleteDbBlockUser", param);
	}

	public int selectDbManagerCheck(ParamMap param) {
		return getSqlSession().selectOne("manageMapper.selectDbManagerCheck", param);
	}
	
	/**
	 * 
	 * @Method Name  : selectUserDbGroup
	 * @Method 설명 : 사용자 권한 있는 db 그룹
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 26. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public List selectUserDbGroup(ParamMap param) {
		return getSqlSession().selectList("manageMapper.selectUserDbGroup", param);
	}

	/**
	 * 
	 * @Method Name  : deleteUserDbGroup
	 * @Method 설명 : 사용자 db권한 제거. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 26. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public int deleteUserDbGroup(ParamMap param) {
		return getSqlSession().delete("manageMapper.deleteUserDbGroup", param);
	}
}
