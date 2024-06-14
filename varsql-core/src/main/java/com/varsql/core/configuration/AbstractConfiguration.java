package com.varsql.core.configuration;

import java.io.File;

import com.vartech.common.utils.StringUtils;


/**
 *
 * @FileName : Configuration.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 28.
 * @프로그램설명: varsql 관련 설정값 읽는 클래스.
 * @변경이력	:
 */
public abstract class AbstractConfiguration{
	
	protected final String VARSQL_INSTALL_PATH = getInstallRoot();
	
	protected final String VARSQL_PROPERTIES_FILE= "config/varsql.properties";
	protected final String QUARTZ_PROPERTIES_FILE= "config/quartz.properties";
	protected final String HIBERNATE_PROPERTIES_FILE= "config/hibernate.properties";
	protected final String CONNECTION_XML_FILE= "config/db.xml";
	
	protected final String LOGBACK_XML_FILE= "config/logback.xml";
	
	public String getInstallRoot() {
		String installRoot = System.getProperty(Constants.CONFIG_PROPERTY_KEY);

		if(installRoot != null && !"".equals(installRoot)) {
			;
		}else {
			
			String catalinaHome = System.getProperty("catalina.home" ,"");

			if(!"".equals(catalinaHome)) {
				installRoot =catalinaHome+File.separator +"resources";
			}else {
				
				String userHome = System.getProperty("user.home");
				installRoot = "C:/zzz/resources/";
				
				if(!StringUtils.isBlank(userHome)) {
					File dir = new File(userHome, ".varsql");
					if(!dir.exists()) {
						dir.mkdir();
					}
					
					installRoot = dir.getAbsolutePath();
				}
			}
		}
		
		installRoot= installRoot.replaceAll("\\\\", "/");
		
		if(installRoot.endsWith("/")) {
			installRoot = installRoot.substring(0, installRoot.length()-1);
		}
		
		return installRoot;
	}
}
