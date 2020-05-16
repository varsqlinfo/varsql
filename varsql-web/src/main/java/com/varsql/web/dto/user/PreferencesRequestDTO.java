package com.varsql.web.dto.user;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.model.entity.user.UserDBPreferencesEntity;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @FileName : PreferencesRequestDTO.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
@Getter
@Setter
public class PreferencesRequestDTO extends DatabaseParamInfo{
	
	private String prefKey;
	private String prefVal;
	
	public UserDBPreferencesEntity toEntity() {
		return UserDBPreferencesEntity.builder()
				.vconnid(getVconnid())
				.viewid(getViewid())
				.prefKey(prefKey)
				.prefVal(prefVal)
				.build();
	}
}
