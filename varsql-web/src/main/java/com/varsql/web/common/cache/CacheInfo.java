package com.varsql.web.common.cache;

public interface CacheInfo{
	final static String CACHE_KEY_TABLE_METADATA = "tableMetadata";
	final static String CACHE_KEY_TABLE_SCRIPT = "tableScript";
	
	enum CacheType {

		TABLE_METADATA(CACHE_KEY_TABLE_METADATA, 60 * 60, 10000) // 60 분 
		, TABLE_SCRIPT(CACHE_KEY_TABLE_SCRIPT, 60 * 60, 10000);	// 60 분

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