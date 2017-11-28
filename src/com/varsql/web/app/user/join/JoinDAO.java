package com.varsql.web.app.user.join;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.user.beans.UserForm;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class JoinDAO extends BaseDAO{
	
	public Object selectUserDetail(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectUserDetail", paramMap);
	}

	public String selectUserMaxVal() {
		return getSqlSession().selectOne("userMapper.selectUserMaxVal");
	}
	
	public int insertUserInfo(UserForm userForm){
		return getSqlSession().insert("userMapper.insertUserInfo", userForm );
	}
	
	public int updateUserInfo(DataCommonVO paramMap){
		return getSqlSession().update("userMapper.updateUserInfo", paramMap);
	}

	public int selectIdCheck(String uid) {
		return getSqlSession().selectOne("userMapper.selectIdCheck", uid);
	}
}
