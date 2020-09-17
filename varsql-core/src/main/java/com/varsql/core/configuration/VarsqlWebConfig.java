package com.varsql.core.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

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
 * @FileName  : VarsqlWebConfig.java
 * @프로그램 설명 : varsql web config
 * @Date      : 2016. 11. 13.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class VarsqlWebConfig extends AbstractXmlLoad{

	private final static Logger logger = LoggerFactory.getLogger(Configuration.class);

	private ParamMap<String,Object> CONFIG_INFO = new ParamMap<String,Object>();
	private Object lock = new Object();

	private String page403CSRF ="/error/page403csrf";
	private String page403 ="/error/page403";
	private String page404 ="/error/page404";
	private String page500 ="/error/page500";
	private String pageHelp ="/common/helpPage";

	private static class Holder {
		private static final VarsqlWebConfig instance = new VarsqlWebConfig();
	}

	public static VarsqlWebConfig newIntance() {
		return Holder.instance;
	}

	private VarsqlWebConfig() {
		try {
			initialize(true);
		} catch (ConfigurationLoadException e) {
			logger.error("VarsqlWebConfig error : {}" ,e.getMessage() , e);
		}
	}

	private void initialize(boolean initflag) throws ConfigurationLoadException {
		synchronized(lock){
			FileInputStream is =null; 
			try{

				logger.info("configuration web xml property : {}",Constants.WEB_CONFIG_FILE);

				File propFile = new File(getInstallRoot(), Constants.WEB_CONFIG_FILE);

				logger.info("config web xml path : {}",propFile);

				if ( ! propFile.canRead() ){

					logger.info("Can't open configuration file path: {}", propFile);

					throw new ConfigurationLoadException( this.getClass().getName() + " - Can't open configuration file path: " + propFile);
				}

				is = new FileInputStream(propFile);

				SAXBuilder builder = new SAXBuilder();
				Element root = builder.build(is).getRootElement();

				getConfigInfo(root);
				
				is.close();
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

		this.page403 = CONFIG_INFO.getString(XmlElementsInfo.PAGE_403.getFieldName(),this.getPage404());
		this.page404 = CONFIG_INFO.getString(XmlElementsInfo.PAGE_404.getFieldName(),this.getPage404());
		this.page500 = CONFIG_INFO.getString(XmlElementsInfo.PAGE_500.getFieldName(),this.getPage500());
		this.page500 = CONFIG_INFO.getString(XmlElementsInfo.PAGE_403CSRF.getFieldName(),this.getPage403CSRF());
	}

	/**
	 *
	 * @Method Name  : getPortletInfo
	 * @Method 설명 : 포틀릿 정보 보기
	 * @작성일   : 2015. 7. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public Map<String, Object> allXmlElementsInfo() {
		return CONFIG_INFO;
	}

	/**
	 * @Method Name  : getPage404
	 * @Method 설명 :
	 * @작성일   : 2015. 9. 8.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @return
	 */
	public String getPage404() {
		return this.page404;
	}

	public String getPage500() {
		return this.page500;
	}

	public String getHelpPage() {
		return this.pageHelp;
	}

	/**
	 *
	* @Method	: getPage403
	* @Method설명	: 권한 없음.
	* @작성일		: 2017. 4. 5.
	* @AUTHOR	: ytkim
	* @변경이력	:
	* @return
	 */
	public String getPage403() {
		return this.page403;
	}

	public String getPage403CSRF() {
		return page403CSRF;
	}

	enum XmlElementsInfo implements XmlField{
		PAGE_403("page403"),PAGE_403CSRF("page403csrf"),PAGE_404("page404"), PAGE_500("page500"), PAGE_HELP("pagehelp");

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
