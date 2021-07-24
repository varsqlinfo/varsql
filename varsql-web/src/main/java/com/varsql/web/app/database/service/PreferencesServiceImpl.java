package com.varsql.web.app.database.service;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.core.configuration.PreferencesDataFactory;
import com.varsql.web.constants.PreferencesConstants;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.model.entity.user.UserDBPreferencesEntity;
import com.varsql.web.repository.spec.UserDBPreferencesSpec;
import com.varsql.web.repository.user.UserDBPreferencesEntityRepository;
import com.varsql.web.util.ValidateUtils;
import com.varsql.web.util.VarsqlUtils;
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
	private final Logger logger = LoggerFactory.getLogger(PreferencesServiceImpl.class);

	@Autowired
	private UserDBPreferencesEntityRepository userDBPreferencesEntityRepository ;

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
	public String selectPreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		return selectPreferencesInfo(preferencesInfo, false);
	}
	public String selectPreferencesInfo(PreferencesRequestDTO preferencesInfo ,boolean flag) {
		UserDBPreferencesEntity item = userDBPreferencesEntityRepository.findOne(UserDBPreferencesSpec.findPrefVal(preferencesInfo)).orElse(null);

		if(item != null) {
			String prefVal = item.getPrefVal();
			if(prefVal != null && !"".equals(prefVal)) {
				return prefVal;
			}
		}

		return (flag ? "{}" : null);
	}


	/**
	 *
	 * @Method Name  : selectPreferencesInfos
	 * @Method 설명 : 설정값 다중으로 가져오기
	 * @작성자   : ytkim
	 * @작성일   : 2020. 8. 15.
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public List<UserDBPreferencesEntity> selectPreferencesInfos(PreferencesRequestDTO... preferencesInfo) {
		List<UserDBPreferencesEntity> items = userDBPreferencesEntityRepository.findAll(UserDBPreferencesSpec.findPrefVal(preferencesInfo));
		return items;
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
	public ResponseResult savePreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		UserDBPreferencesEntity userDBPreferencesEntity = userDBPreferencesEntityRepository.findOne(UserDBPreferencesSpec.findPrefVal(preferencesInfo)).orElse(null);

		if(userDBPreferencesEntity==null) {
			userDBPreferencesEntity = preferencesInfo.toEntity();
		}else {
			userDBPreferencesEntity.setPrefVal(preferencesInfo.getPrefVal());
		}

		userDBPreferencesEntityRepository.save(userDBPreferencesEntity);

		return VarsqlUtils.getResponseResultItemOne(1);
	}
	
	/**
	 *
	 * @Method Name  : findMainSettingInfo
	 * @Method 설명 : main 설정 정보
	 * @작성자   : ytkim
	 * @작성일   : 2020. 10. 12.
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public HashMap<String ,String> findMainSettingInfo(PreferencesRequestDTO preferencesInfo) throws IllegalAccessException, InvocationTargetException {
		
		preferencesInfo.setPrefKey(PreferencesConstants.PREFKEY.MAIN_SETTING.key());
		
		PreferencesRequestDTO contextMenuKey = new PreferencesRequestDTO();
		BeanUtilsBean.getInstance().copyProperties(contextMenuKey, preferencesInfo);
		contextMenuKey.setPrefKey(PreferencesConstants.PREFKEY.CONTEXTMENU_SERVICEOBJECT.key());
		
		HashMap<String ,String> returnVal = new HashMap<String,String>();
		this.selectPreferencesInfos(preferencesInfo, contextMenuKey).forEach(item ->{
			String key = item.getPrefKey();
			String prefVal = item.getPrefVal();
			returnVal.put(key, prefVal);
		});
		
		if(ValidateUtils.isMapValueBlank(returnVal, PreferencesConstants.PREFKEY.CONTEXTMENU_SERVICEOBJECT.key())) {
			returnVal.put(PreferencesConstants.PREFKEY.CONTEXTMENU_SERVICEOBJECT.key(), PreferencesDataFactory.getInstance().getDefaultValue(PreferencesConstants.PREFKEY.CONTEXTMENU_SERVICEOBJECT.key()));
		}
		
		if(ValidateUtils.isMapValueBlank(returnVal, PreferencesConstants.PREFKEY.MAIN_SETTING.key())) {
			returnVal.put(PreferencesConstants.PREFKEY.MAIN_SETTING.key(), "{}");
		}
		
		return returnVal;
	}
}