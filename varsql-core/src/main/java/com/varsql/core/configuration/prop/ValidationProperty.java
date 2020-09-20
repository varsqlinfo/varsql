package com.varsql.core.configuration.prop;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ValidationProperty{
	private Logger log = LoggerFactory.getLogger(ValidationProperty.class);

	final String Config_File= "db/validation.properties";

	private Properties props = new Properties();

	private ValidationProperty(){
		initialize();
	}

	protected void initialize() {
		try{
			initConfig();
		}catch(Exception e){
			log.error(this.getClass().getName(), e);
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

		File propFile = new File(Configuration.getInstance().getInstallRoot(), Config_File);

		log.info("Validation property file : {}",propFile);

		if ( ! propFile.canRead() ){

			log.info("Can't open Validation file path: {}", propFile);

			throw new ConfigurationException( this.getClass().getName() + " - Can't open Validation file path: " + propFile);
		}

		FileInputStream jdf_fin = new FileInputStream(propFile);

		try{
			props.load(jdf_fin);
			jdf_fin.close();
		}finally{
			if(jdf_fin != null) jdf_fin.close();
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
