package com.varsql.web.app.user.join;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.user.beans.JoinForm;
import com.varsql.web.dao.BaseDAO;


@Repository
public class JoinDAO extends BaseDAO{
	
	public String selectUserMaxVal() {
		return getSqlSession().selectOne("userMapper.selectUserMaxVal");
	}
	
	public int insertUserInfo(JoinForm	joinForm){
		return getSqlSession().insert("userMapper.insertUserInfo", joinForm);
	}
	
	public int selectIdCheck(String uid) {
		return getSqlSession().selectOne("userMapper.selectIdCheck", uid);
	}
}
