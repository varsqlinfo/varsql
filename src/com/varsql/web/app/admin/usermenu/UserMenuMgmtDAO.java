package com.varsql.web.app.admin.usermenu;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class UserMenuMgmtDAO extends BaseDAO{
	
	public int listDbMenuTotalcnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("listDbMenuTotalcnt", paramMap);
	}

	public Object listDbMenu(DataCommonVO paramMap) {
		return getSqlSession().selectList("listDbMenu", paramMap);
	}

	public int moodifyDbMenu(DataCommonVO paramMap) {
		return getSqlSession().update("moodifyDbMenu", paramMap);
	}

	public int addDbMenu(DataCommonVO paramMap) {
		return getSqlSession().insert("addDbMenu", paramMap);
	}

	public int deleteDbMenu(DataCommonVO paramMap) {
		return getSqlSession().delete("deleteDbMenu", paramMap);
	}
}
