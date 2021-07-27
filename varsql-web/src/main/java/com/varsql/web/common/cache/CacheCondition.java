package com.varsql.web.common.cache;

import org.springframework.stereotype.Component;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.constants.ResourceConfigConstants;

@Component(ResourceConfigConstants.CACHE_CONDITION_COMPONENT)
public class CacheCondition {
	
	public boolean dbObjectListCondition(DatabaseParamInfo databaseParamInfo) {
		
		System.out.println("1111111111111111");
		System.out.println("1111111111111111");
		System.out.println("1111111111111111");
		
		if(databaseParamInfo.isRefresh()) {
			return false; 
		}
		
		return false; 
	}
}
