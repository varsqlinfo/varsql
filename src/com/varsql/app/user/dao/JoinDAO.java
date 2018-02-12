package com.varsql.app.user.dao;

import org.springframework.stereotype.Repository;

import com.varsql.app.dao.BaseDAO;
import com.varsql.app.user.beans.JoinForm;


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
