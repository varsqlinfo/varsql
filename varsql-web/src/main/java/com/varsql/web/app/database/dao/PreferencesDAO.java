package com.varsql.web.app.database.dao;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.dao.BaseDAO;
import com.varsql.web.dto.user.PreferencesRequestDTO;

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
	 * @param b 
	 * @return
	 */
	public String selectPreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		return selectPreferencesInfo(preferencesInfo, false);
	}
	public String selectPreferencesInfo(PreferencesRequestDTO preferencesInfo, boolean defaultVal) {
		DataCommonVO dcv = getSqlSession().selectOne("userPreferencesMapper.selectPreferencesInfo", preferencesInfo);
		
		if(dcv ==null){
			if(defaultVal){
				dcv = new DataCommonVO();
				dcv.put("PREF_VAL","{}");
			}else{
				return null; 
			}
		}
		return dcv.getString("PREF_VAL","{}");
	}
	
	/**
	 * 
	 * @Method Name  : insertPreferencesInfo
	 * @Method 설명 : 설정 정보 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 23. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public int insertPreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		return getSqlSession().insert("userPreferencesMapper.insertPreferencesInfo", preferencesInfo);
	}
	
	/**
	 * 
	 * @Method Name  : updatePreferencesInfo
	 * @Method 설명 : 설정 정보 업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 23. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public int updatePreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		return getSqlSession().insert("userPreferencesMapper.updatePreferencesInfo", preferencesInfo);
	}
}
