package com.varsql.web.app.database.tools.setting;

import org.springframework.stereotype.Repository;

import com.varsql.web.app.database.bean.PreferencesInfo;
import com.varsql.web.dao.BaseDAO;

@Repository
public class PreferencesDAO extends BaseDAO{
	/**
	 * 
	 * @Method Name  : selectPreferencesInfo
	 * @Method 설명 : 사용자 환경 설정 정보 얻기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 22. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public Object selectPreferencesInfo(PreferencesInfo preferencesInfo) {
		return getSqlSession().selectOne("userPreferencesMapper.selectPreferencesInfo", preferencesInfo);
	}
}
