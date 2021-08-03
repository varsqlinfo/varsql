package com.varsql.web.common.cache;

import com.varsql.core.db.valueobject.DatabaseParamInfo;

public final class CacheUtils {

	public static String getObjectTypeKey(DatabaseParamInfo databaseParamInfo) {
		return databaseParamInfo.getVconnid() +"_" + databaseParamInfo.getSchema() +"_"+ databaseParamInfo.getObjectType() ;
	}
}
