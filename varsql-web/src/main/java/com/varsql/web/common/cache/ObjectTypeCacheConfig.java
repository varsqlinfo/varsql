package com.varsql.web.common.cache;

import org.springframework.stereotype.Component;

import com.varsql.core.db.valueobject.DatabaseParamInfo;

@Component(CacheInfo.CACHE_KEY_OBJECTYPE_METADATA)
public class ObjectTypeCacheConfig {

	public String dbObjectTypeKey(DatabaseParamInfo databaseParamInfo) {
		return CacheUtils.getObjectTypeKey(databaseParamInfo);
	}

	public boolean dbObjectListCondition(DatabaseParamInfo databaseParamInfo) {
		if(databaseParamInfo.isRefresh()) {
			if(databaseParamInfo.getObjectNames() != null && databaseParamInfo.getObjectNames().length > 0) {
				return false;
			}

			if(!databaseParamInfo.isBaseSchemaFlag()) {
				return false;
			}
		}

		return true;
	}
}


