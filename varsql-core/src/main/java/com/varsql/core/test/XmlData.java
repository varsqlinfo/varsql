package com.varsql.core.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.varsql.core.connection.beans.ConnectionInfo;

public class XmlData {
	public static void main(String[] args) {
		new XmlData().execute();
	}

	private void execute() {
		String xmlpath = "C:/01.HKMC/04.source/varsql/resource/config/varsqlConnectionConfig.xml";

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(xmlpath);
		
		ConnectionInfo connectionConfig = new ConnectionInfo();

		try {
			
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();

			Element dataSource = rootNode.getChild("dataSource");

			connectionConfig.setAliasName(dataSource.getAttributeValue("name"));
			connectionConfig.setType(getPropertyValue(rootNode, "type"));
			connectionConfig.setDriver(getPropertyValue(rootNode,"driver"));
			connectionConfig.setUrl(getPropertyValue(rootNode,"url"));
			connectionConfig.setUsername(getPropertyValue(rootNode,"username"));
			connectionConfig.setPassword(getPropertyValue(rootNode,"password"));
			//connectionConfig.setMax_active(getPropertyValue(rootNode,"max_active"));
			connectionConfig.setTimebetweenevictionrunsmillis(Long.parseLong(getPropertyValue(rootNode,"timebetweenevictionrunsmillis")));
			connectionConfig.setTest_while_idle(getPropertyValue(rootNode,"test_while_idle"));
			
			System.out.println(connectionConfig);
			

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	private String getPropertyValue(Element rootNode, String nm) throws JDOMException {
		Object e = XPath.selectSingleNode(rootNode, "//property[@name='"+nm+"']"); 
		
		return e==null?"":((Element)e).getAttributeValue("value");
	}
}
