package com.varsql.core.configuration;

import java.io.File;


/**
 *
 * @FileName : ConfigurationFilePath.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 28.
 * @프로그램설명: 
 * @변경이력	:
 */
public class ConfigurationFilePath extends AbstractConfiguration{
	
	private static class ConfigurationFilePathHolder{
        private static final ConfigurationFilePath instance = new ConfigurationFilePath();
    }

	public static ConfigurationFilePath getInstance() {
		return ConfigurationFilePathHolder.instance;
    }

	/**
	 * system 프로퍼티 셋팅
	 *
	 * @method : setSystemProperties
	 */
	public void setSystemProperties() {
		//log back 경로 설정
		File file = new File(VARSQL_INSTALL_PATH, LOGBACK_XML_FILE);
		
		if(file != null && file.exists()) {
			System.setProperty("logback.configurationFile", file.getAbsolutePath());
		}
		
		String catalinaHome = System.getProperty("catalina.home" ,"");

		if("".equals(catalinaHome)) {
			System.setProperty(Constants.RUNTIME_KEY, "local");
			System.setProperty(Constants.CONFIG_PROPERTY_KEY, getInstallRoot());
			System.setProperty("spring.devtools.restart.enabled", "true");
			System.setProperty("spring.devtools.livereload.enable", "true");
		}
	}
}
