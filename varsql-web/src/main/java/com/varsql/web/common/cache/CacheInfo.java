package com.varsql.web.common.cache;

public interface CacheInfo{
	final static String CACHE_KEY_OBJECTYPE_METADATA = "objectMetadata";
	final static String CACHE_KEY_OBJECTYPE_SCRIPT = "objectScript";

	enum CacheType {

		OBJECT_TYPE_METADATA(CACHE_KEY_OBJECTYPE_METADATA, 60 * 60 * 12, 10000) // 12시간
		, OBJECT_TYPE_SCRIPT(CACHE_KEY_OBJECTYPE_SCRIPT, 60 * 60 * 12, 10000);	// 12시간

		private String cacheName;
		private int expiredAfterWrite;
		private int maximumSize;

		CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
			this.cacheName = cacheName;
			this.expiredAfterWrite = expiredAfterWrite;
			this.maximumSize = maximumSize;
		}

		public String getCacheName() {
			return cacheName;
		}

		public int getExpiredAfterWrite() {
			return expiredAfterWrite;
		}

		public int getMaximumSize() {
			return maximumSize;
		}

	}


}