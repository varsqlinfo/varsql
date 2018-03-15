package com.varsql.app.database.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.database.beans.PreferencesInfo;
import com.varsql.app.database.dao.PreferencesDAO;

/**
 * 
 * @FileName  : PreferencesServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class PreferencesServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(PreferencesServiceImpl.class);
	
	@Autowired
	private PreferencesDAO preferencesDAO ;
	
	public String selectPreferencesInfo(PreferencesInfo preferencesInfo) {
		return preferencesDAO.selectPreferencesInfo(preferencesInfo, false);
	}
}