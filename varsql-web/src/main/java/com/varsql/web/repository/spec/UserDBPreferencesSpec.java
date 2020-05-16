package com.varsql.web.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.model.entity.user.UserDBPreferencesEntity;

/**
 * -----------------------------------------------------------------------------
* @fileName		: UserDBPreferencesSpec.java
* @desc		: db에 대한 사용자 환경 설정 정보. 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class UserDBPreferencesSpec extends DefaultSpec{

	public static Specification<UserDBPreferencesEntity> findPrefVal(PreferencesRequestDTO preferencesInfo) {
		return (root, query, cb) -> {
			return cb.and(
				cb.equal(root.get(UserDBPreferencesEntity.VCONNID), preferencesInfo.getVconnid())
				, cb.equal(root.get(UserDBPreferencesEntity.VIEWID), SecurityUtil.userViewId())
				, cb.equal(root.get(UserDBPreferencesEntity.PREF_KEY), preferencesInfo.getPrefKey())
			);
		};
	}
    
}