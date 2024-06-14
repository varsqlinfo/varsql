package com.varsql.web.dto.user;

import com.varsql.web.model.entity.user.UserDBPreferencesEntity;
import com.varsql.web.util.DatabaseUtils;
import com.varsql.web.util.SecurityUtil;

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
public class PreferencesRequestDTO{
	
	private String conuid;
	private String prefKey;
	private String prefVal;
	private String vconnid;
	
	public UserDBPreferencesEntity toEntity() {
		return UserDBPreferencesEntity.builder()
				.vconnid(this.vconnid)
				.viewid(SecurityUtil.userViewId())
				.prefKey(this.prefKey)
				.prefVal(this.prefVal)
				.build();
	}
	
	
	public void setConuid(String conuid) {
		this.conuid = conuid;
		
		this.vconnid = DatabaseUtils.convertConUidToVconnid(conuid);
		
		//setDatabaseInfo(SecurityUtil.loginInfo().getDatabaseInfo().get(conuid));
	}
}
