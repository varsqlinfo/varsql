package com.varsql.web.common.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.model.entity.db.DBConnectionEntity;

public final class CacheUtils {

	private static final String CACKE_KEY_DELIMETER ="_";

	public static String getObjectTypeKey(DatabaseParamInfo databaseParamInfo) {
		return databaseParamInfo.getVconnid() +CACKE_KEY_DELIMETER + databaseParamInfo.getSchema() +CACKE_KEY_DELIMETER+ databaseParamInfo.getObjectType() ;
	}

	public static void removeObjectCache(CacheManager cacheManager, DBConnectionEntity reqEntity) {
		removeObjectCache(cacheManager, reqEntity.getVconnid());
	}

	public static void removeObjectCache(CacheManager cacheManager, String vconnid) {
		for (CacheInfo.CacheType cacheType: CacheInfo.CacheType.values()) {
			Cache cache = cacheManager.getCache(cacheType.getCacheName());

			com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = ((CaffeineCache)cache).getNativeCache();
		    nativeCache.asMap().keySet().forEach(key ->{
		    	if(key != null && key.toString().startsWith(vconnid+CACKE_KEY_DELIMETER)) {
		    		cache.evict(key);
				}
		    });

		}

	}
}
