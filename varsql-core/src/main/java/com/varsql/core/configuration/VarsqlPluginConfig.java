package com.varsql.core.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.configuration.xml.AbstractXmlLoad;
import com.varsql.core.configuration.xml.XmlField;
import com.varsql.core.exception.ConfigurationLoadException;
import com.vartech.common.app.beans.ParamMap;

/**
 *
 * @FileName  : VarsqlPluginConfig.java
 * @프로그램 설명 : varsql plugin config
 * @Date      : 2019. 11. 14.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlPluginConfig extends AbstractXmlLoad{

	private final Logger logger = LoggerFactory.getLogger(Configuration.class);

	private ParamMap<String,Object> CONFIG_INFO = new ParamMap<String,Object>();
	private Object lock = new Object();


	private String[] packageArr;

	private static class Holder {
		private static final VarsqlPluginConfig instance = new VarsqlPluginConfig();
	}

	public static VarsqlPluginConfig newIntance() {
		return Holder.instance;
	}

	private VarsqlPluginConfig() {
		try {
			initialize(true);
		} catch (ConfigurationLoadException e) {
			logger.error("varsql plugin init error : {}" , e.getMessage());
			logger.error("varsql plugin init error" , logger);
		}
	}

	@SuppressWarnings("unused")
	private void initialize(boolean initflag) throws ConfigurationLoadException {
		FileInputStream is =null;
		synchronized(lock){
			try{

				logger.info("configuration plugin xml property : {}",Constants.PLUGIN_CONFIG_FILE);

				File propFile = new File(getInstallRoot(), Constants.PLUGIN_CONFIG_FILE);

				logger.info("config plugin xml path : {}",propFile);

				if ( ! propFile.canRead() ){

					logger.info("Can't open configuration file path: {}", propFile);

					throw new ConfigurationLoadException( this.getClass().getName() + " - Can't open configuration file path: " + propFile);
				}

				is = new FileInputStream(propFile);

				SAXBuilder builder = new SAXBuilder();
				Element root = builder.build(is).getRootElement();

				getConfigInfo(root);
			}catch(ConfigurationLoadException e) {
				throw new ConfigurationLoadException( this.getClass().getName() +  e.getMessage());
			}catch(Exception e){
				throw new ConfigurationLoadException( this.getClass().getName() + e.getLocalizedMessage()+"\n"+ e.getMessage());
			}finally {
				if(is != null) try{is.close();}catch(Exception e) {}
			}
		} // end of sunchronized(lock);
	}

	private void getConfigInfo(Element root) throws RuntimeException {
		CONFIG_INFO = getXmlElementsInfo(root);

		Object packageObj= ((ParamMap)CONFIG_INFO.get(XmlElementsInfo.PACKAGES.getFieldName())).get(XmlElementsInfo.PACKAGE.getFieldName());

		if(packageObj instanceof List) {
			List packageList = ((List)packageObj);
			packageArr = (String[])packageList.toArray(new String[packageList.size()]);
		}else {
			packageArr = new String[1];
			packageArr[0] = packageObj.toString();
		}
	}

	public String[] getPackageArr() {
		return this.packageArr;
	}

	enum XmlElementsInfo implements XmlField{
		PACKAGES("packages"),PACKAGE("package");

		private String fieldName;
		private String[] fieldAttr;
		private XmlField.OBJECT_TYPE objectType;
		XmlElementsInfo(String _fieldName){
			this.fieldName = _fieldName;
			this.objectType = XmlField.OBJECT_TYPE.OBJECT;
		}
		XmlElementsInfo(String _fieldName,XmlField.OBJECT_TYPE _objectType){
			this.fieldName = _fieldName;
			this.objectType = _objectType;
		}

		XmlElementsInfo(String _fieldName,XmlField.OBJECT_TYPE _objectType, String ... attr){
			this.fieldName = _fieldName;
			this.objectType = _objectType;
			this.fieldAttr = attr;
		}

		@Override
		public String getFieldName(){
			return this.fieldName;
		}

		@Override
		public String[] getFieldAttr() {
			return this.fieldAttr;
		}
		@Override
		public OBJECT_TYPE getObjectType() {
			return this.objectType;
		}
	}
}
