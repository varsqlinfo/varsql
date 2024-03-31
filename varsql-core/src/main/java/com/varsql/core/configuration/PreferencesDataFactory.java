package com.varsql.core.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.configuration.beans.pref.AbstractPreferences;
import com.varsql.core.configuration.beans.pref.PreferencesContextMenu;
import com.varsql.core.configuration.beans.pref.PreferencesConvertText;
import com.vartech.common.utils.VartechReflectionUtils;
import com.vartech.common.utils.VartechUtils;


/**
 * varsql 환경설정 기본 값
* 
* @fileName	: PreferencesDataFactory.java
* @author	: ytkim
 */
public class PreferencesDataFactory{
	private final Logger logger = LoggerFactory.getLogger(Configuration.class);

	private final String TEMPLATE_PACKAGE= "classpath*:preferences/*/*.xml";
	private final String DEFAULT_KEY_NAME= "key";
	
	
	private enum PREFERENCES_FILE {
		CONTEXT_MENU("contextMenu.xml", PreferencesContextMenu.class)
		,CONVERT_TEXT("convertText.xml", PreferencesConvertText.class);
		
		private String fileName;
		private Class<?> beanClass;

		PREFERENCES_FILE(String fileName, Class<?> class1) {
			this.fileName = fileName;
			this.beanClass = class1;
		}

		public String getFileName() {
			return fileName;
		}

		public Class<?> getBeanClass() {
			return beanClass;
		}
		
		public static PREFERENCES_FILE get(String fileName) {
			for (PREFERENCES_FILE info : values()) {
				if(info.getFileName().equals(fileName)) {
					return info; 
				}
			}
			return null; 
		}
	}

	private Map<String ,String> defaultPreferencesInfo = new HashMap<String,String>();

	private static class ConfigurationHolder{
        private static final PreferencesDataFactory instance = new PreferencesDataFactory();
    }

	public static PreferencesDataFactory getInstance() {
		return ConfigurationHolder.instance;
    }

	private PreferencesDataFactory(){
		initialize();
	}

	protected void initialize() {
		try{
			initConfig();
		}catch(Exception e){
			logger.error(this.getClass().getName(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * initialize config file
	 * @throws IOException
	 * @Method Name  : initConfig
	 * @Author : ytkim
	 * @Method desc :
	 * @History :
	 */
	private void initConfig() throws IOException {
		logger.debug("default preferences template file path : {} ", TEMPLATE_PACKAGE);

		Resource[] resources = ResourceUtils.getResources(TEMPLATE_PACKAGE);
		
		for (Resource resource: resources){
			PREFERENCES_FILE prefInfo = PREFERENCES_FILE.get(resource.getFilename());
			
			String xml = ResourceUtils.getResourceString(resource);
			
			String key;
			
			if(prefInfo != null) {
				AbstractPreferences xmlConfigObj = (AbstractPreferences) VartechUtils.xmlToBean(xml, prefInfo.getBeanClass());
				
				key=(String) VartechReflectionUtils.getProperty(xmlConfigObj, DEFAULT_KEY_NAME);
				
				if(key == null) {
					logger.debug("key empty resource name : {} , path : {} , xml : {}",resource.getFilename() ,resource.getFile().getPath() , xml);
					continue;
				}
				
				defaultPreferencesInfo.put(key, VartechUtils.objectToJsonString(xmlConfigObj.getRootItem()));
			}else {
				
				ObjectNode node = (ObjectNode)VartechUtils.xmlToJsonNode(xml);
				if(node.get(DEFAULT_KEY_NAME) == null) {
					logger.debug("key empty resource name : {} , path : {} , xml : {}",resource.getFilename() ,resource.getFile().getPath() , xml);
					continue;
				}
				
				key =node.get(DEFAULT_KEY_NAME).asText();
				
				node.remove(DEFAULT_KEY_NAME);
				
				defaultPreferencesInfo.put(key, VartechUtils.objectToJsonString(node));
			}
		}
	}

	public String getDefaultValue(String key) {
		return defaultPreferencesInfo.get(key);
	}
}
