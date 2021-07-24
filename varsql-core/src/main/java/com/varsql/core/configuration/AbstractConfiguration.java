package com.varsql.core.configuration;

import java.io.File;


/**
 *
 * @FileName : Configuration.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 28.
 * @프로그램설명: varsql 관련 설정값 읽는 클래스.
 * @변경이력	:
 */
public abstract class AbstractConfiguration{
	public String getInstallRoot() {
		String installRoot = System.getProperty(Constants.VARSQL_INSTALL_ROOT);

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
}
