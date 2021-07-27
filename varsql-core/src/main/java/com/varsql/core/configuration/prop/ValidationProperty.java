package com.varsql.core.configuration.prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.configuration.AbstractConfiguration;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.configuration.Constants;
import com.varsql.core.exception.ConfigurationException;


/**
 *
 * @FileName : ValidationProperty.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 28.
 * @프로그램설명: query validation check 클래스.
 * @변경이력	:
 */
public class ValidationProperty extends AbstractConfiguration{
	private Logger logger = LoggerFactory.getLogger(ValidationProperty.class);

	final String CONFIG_FILE= "db/validation.properties";

	private Properties props = new Properties();

	private ValidationProperty(){
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
	 * @Method Name  : initConfig
	 * @Author : ytkim
	 * @Method desc :
	 * @History :
	 * @throws Exception
	 */
	private void initConfig() throws Exception {
		
		Resource configResource = null;

		File propFile = new File(getInstallRoot(), CONFIG_FILE);
		
		logger.info("Validation Property path: {}",propFile);
		
		if(propFile.exists()) {
			configResource = ResourceUtils.getResource(propFile.getPath());
		}else {
			configResource = ResourceUtils.getResource(CONFIG_FILE);
		}

		if ( configResource == null ){
			logger.info("Can't open Validation file path: {}", propFile);
			throw new ConfigurationException( this.getClass().getName() + " - Can't open Validation file path: " + propFile);
		}

		try(InputStream is  = configResource.getInputStream()){
			props.load(is);
		}catch(IOException e) {
			throw new ConfigurationException(e);
		}
	}

	public String validationQuery(String dbtype) {
		String val = props.getProperty(dbtype.toUpperCase(),"");
		return !"".equals(val) ? val :props.getProperty("ETC") ;
	}

	private static class ValidationHolder{
        private static final ValidationProperty instance = new ValidationProperty();
    }

	public static ValidationProperty getInstance() {
		return ValidationHolder.instance;
    }
}
