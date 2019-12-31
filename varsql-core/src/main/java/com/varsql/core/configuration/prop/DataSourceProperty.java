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
 * @FileName : DataSourceProperty.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 28.
 * @프로그램설명: datasource class  클래스. 
 * @변경이력	:
 */
public class DataSourceProperty{
	Logger log = LoggerFactory.getLogger(DataSourceProperty.class);
	
	final String Config_File= "etc/datasourceConfig.properties";
	
	private Properties props = new Properties();
	
	private DataSourceProperty(){
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
		
		log.info("DataSourceProperty property file : {}",propFile);
		
		if ( ! propFile.canRead() ){
			
			log.info("Can't open DataSourceProperty file path: {}", propFile);
			
			throw new ConfigurationException( this.getClass().getName() + " - Can't open DataSourceProperty file path: " + propFile);
		}
			
		FileInputStream jdf_fin = new FileInputStream(propFile);
		
		try{
			props.load(jdf_fin);
			jdf_fin.close();
		}finally{
			if(jdf_fin != null) jdf_fin.close();
		}
	}

	public String getProp(String key) {
		return props.getProperty(key);
	}
	
	private static class Holder{
        private static final DataSourceProperty instance = new DataSourceProperty();
    }
	
	public static DataSourceProperty getInstance() {
		return Holder.instance;
    }
}
