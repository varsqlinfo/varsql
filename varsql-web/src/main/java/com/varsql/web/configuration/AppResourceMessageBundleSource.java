package com.varsql.web.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.utils.VartechUtils;

public class AppResourceMessageBundleSource extends ReloadableResourceBundleMessageSource {
	
	private final Logger logger = LoggerFactory.getLogger(AppResourceMessageBundleSource.class);
	
	private static final ConcurrentMap<Locale, String> cachedMergedMap = new ConcurrentHashMap<>();
	private static final ConcurrentMap<Locale, Long> reloadTime = new ConcurrentHashMap<>();
	
	private static boolean mergePropLoadFlag = false; 
	private static long versionTime = System.currentTimeMillis();
	
	Set<String> reloadLocale = new HashSet<>();
	
	@Override
	protected Properties loadProperties(Resource resource, String fileName) throws IOException {
		logger.debug("loadProperties : {}", fileName);
		
		Properties prop = super.loadProperties(resource, fileName);
		
		int lastIdx = fileName.lastIndexOf("_");
		
		String localeCode = lastIdx > 0 ? fileName.substring(lastIdx+1) : ""; 
		
		reloadLocale.add(localeCode);
		
		if(mergePropLoadFlag) {
			if(StringUtils.isNotBlank(localeCode)) {
				Locale locale = LocaleConstants.parseLocaleString(localeCode);
				
				if(reloadTime.containsKey(locale)) {
					reloadTime.put(locale, System.currentTimeMillis());
				}else {
					reloadTime.put(locale, versionTime);
				}
			}else {
				for(LocaleConstants code :  LocaleConstants.values()) {
					Locale locale = new Locale(code.getLocaleCode());
					
					if(reloadTime.containsKey(locale)) {
						reloadTime.put(locale, System.currentTimeMillis());
					}else {
						reloadTime.put(locale, versionTime);
					}
				}
			}
		}
		
		return prop;
	}
	
	public String getMergedJsonString(Locale locale) {
		mergePropLoadFlag = true; 
		
		HashMap<String, Object> mergedMap = new HashMap<String, Object>();
		String[] basenames = org.springframework.util.StringUtils.toStringArray(getBasenameSet());
		
		for (int i = basenames.length - 1; i >= 0; i--) {
			List<String> filenames = calculateAllFilenames(basenames[i], locale);
			for (int j = filenames.size() - 1; j >= 0; j--) {
				String filename = filenames.get(j);
				PropertiesHolder propHolder = getProperties(filename);
				
				Properties prop = propHolder.getProperties();
				if (prop != null) {
					
					prop.entrySet().forEach(item->{
						mergedMap.put((String) item.getKey(), item.getValue());
					});
				}
			}
		}
		
		cachedMergedMap.put(locale, "/*"+locale+"*/ var VARSQL_LANG = "+VartechUtils.objectToJsonString(mergedMap));
		
		return cachedMergedMap.get(locale);
	}

	public static long getLastLoadTime(Locale locale) {
		return reloadTime.getOrDefault(locale, versionTime);
	}
	
	@Override
	protected long getCacheMillis() {
		if(VarsqlUtils.isRuntimeLocal()) {
			return 3*1000;
		}else {
			return super.getCacheMillis();
		}
		
		
	}
}