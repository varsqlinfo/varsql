package com.varsql.core.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.vartech.common.utils.VartechUtils;


/**
 *
 * @FileName : PreferencesDataFactory.java
 * @작성자 	 : ytkim
 * @Date	 : 2020. 09. 04.
 * @프로그램설명: varsql 환경설정 기본 값 
 * @변경이력	:
 */
public class PreferencesDataFactory{
	private final static Logger logger = LoggerFactory.getLogger(Configuration.class);

	XPath xpath = XPathFactory.newInstance().newXPath();

	private final String TEMPLATE_PACKAGE= "classpath*:preferences/*/*.xml";
	private final String DEFAULT_KEY_NAME= "key";

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
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = resolver.getResources(TEMPLATE_PACKAGE) ;
		
		for (Resource resource: resources){
			
			String xml = IOUtils.toString(resource.getInputStream(), Charset.forName(VarsqlConstants.CHAR_SET));
			
			JsonNode node = VartechUtils.xmlToJsonNode(xml);
			
			if(node.get(DEFAULT_KEY_NAME) == null) {
				logger.debug("key empty resource name : {} , path : {} ",resource.getFilename() ,resource.getFile().getPath());
				continue; 
			}
			
			String key =node.get(DEFAULT_KEY_NAME).asText();

			defaultPreferencesInfo.put(key, VartechUtils.objectToJsonString(node));
		}
	}
	
	public String getDefaultValue(String key) {
		return defaultPreferencesInfo.get(key);
	}
}
