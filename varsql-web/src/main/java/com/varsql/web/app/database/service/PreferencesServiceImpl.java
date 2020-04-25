package com.varsql.web.app.database.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.app.database.beans.PreferencesInfo;
import com.varsql.web.app.database.dao.PreferencesDAO;
import com.vartech.common.app.beans.ResponseResult;

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
	
	/**
	 * 
	 * @Method Name  : selectPreferencesInfo
	 * @Method 설명 : 설정 정보 가져오기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 3. 16. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public String selectPreferencesInfo(PreferencesInfo preferencesInfo) {
		return selectPreferencesInfo(preferencesInfo, false);
	}
	public String selectPreferencesInfo(PreferencesInfo preferencesInfo ,boolean flag) {
		return preferencesDAO.selectPreferencesInfo(preferencesInfo, flag);
	}
	
	/**
	 * 
	 * @Method Name  : savePreferencesInfo
	 * @Method 설명 : 설정 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 3. 16. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public ResponseResult savePreferencesInfo(PreferencesInfo preferencesInfo) {
		ResponseResult responseResult = new ResponseResult();
		int result = 0; 
		if(preferencesDAO.selectPreferencesInfo(preferencesInfo)==null){
			result =preferencesDAO.insertPreferencesInfo(preferencesInfo);
		}else{
			result = preferencesDAO.updatePreferencesInfo(preferencesInfo);
		}
		responseResult.setItemOne(result);
		return responseResult; 
	}
}