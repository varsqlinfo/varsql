package com.varsql.core.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.varsql.core.connection.ConnectionContext;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.exception.ConfigurationException;
import com.vartech.common.encryption.PasswordType;


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
			installRoot =System.getProperty("catalina.home")+File.separator +"resources"; 
		}
		
		return installRoot.replaceAll("\\\\", "/"); 
	}
}
