package com.varsql.core.configuration;

import java.io.File;

import org.springframework.core.io.Resource;

import com.varsql.core.common.util.ResourceUtils;


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
	
	protected final String VARSQL_PROPERTIES_FILE= "config/varsqlConfig.properties";
	protected final String VARSQL_QUARTZ_PROPERTIES_FILE= "config/varsqlQuartz.properties";
	protected final String VARSQL_HIBERNATE_PROPERTIES_FILE= "config/hibernate.properties";
	protected final String CONNECTION_XML_FILE= "config/varsqlConnectionConfig.xml";
	
	protected final String LOGBACK_XML_FILE= "config/logback.xml";
	
	public String getInstallRoot() {
		String installRoot = System.getProperty(Constants.CONFIG_PROPERTY_KEY);

		if(installRoot != null && !"".equals(installRoot)) {
			;
		}else {
			String catalinaHome = System.getProperty("catalina.home" ,"");
			if(!"".equals(catalinaHome)) {
				installRoot =System.getProperty("catalina.home")+File.separator +"resources";
			}else {
				return Thread.currentThread().getContextClassLoader().getResource("").getFile();
			}
		}
		
		installRoot= installRoot.replaceAll("\\\\", "/");
		
		if(installRoot.endsWith("/")) {
			installRoot = installRoot.substring(0, installRoot.length()-1);
		}
		
		return installRoot;
	}
	
	private Resource getResourceFile(String filePath) {
		File file = new File(VARSQL_INSTALL_PATH, filePath);
		if(file.exists()) {
			return ResourceUtils.getResource(file.getPath());
		}else {
			return ResourceUtils.getResource(filePath);
		}
	}
}
