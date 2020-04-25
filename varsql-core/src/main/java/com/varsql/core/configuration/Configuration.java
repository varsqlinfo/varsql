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
public class Configuration extends AbstractConfiguration{
	private final static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	final String VARSQL_INSTALL_PATH = getInstallRoot();
	final String CONFIG_FILE= "etc/varsqlConfig.properties";
	final String CONNECTION_FILE= "config/varsqlConnectionConfig.xml";
	
	private Properties props = new Properties();
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	private ConnectionInfo vConInfo = new ConnectionInfo();
	
	private final String TYPE = "varsql.type";
	
	private final String USE_CONNID_KEY = "varsql.connid.uuid.use";
	private final String INIT_PASSWORD_TYPE = "varsql.init.password.type";
	private final String INIT_PASSWORD_SIZE = "varsql.init.password.size";
	private final String FILE_UPLOAD_PATH = "file.upload.path";
	private final String FILE_UPLOAD_SIZE = "file.upload.size";
	private final String FILE_UPLOAD_SIZEPERFILE = "file.upload.sizeperfile";

	private PasswordType passwordType;
	
	private int passwordLen;
	
	private boolean useConnUID = true; 
	
	private String fileUploadPath="";
	
	private int fileUploadSize=0;
	
	private int fileUploadSizePerFile=0;
	
	private Configuration(){
		initialize();
	}
	
	protected void initialize() {
		try{
			initConfig();
			initConnection();
		}catch(Exception e){
			logger.error(this.getClass().getName(), e);
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
		String configPropFile = getSystemProperty(Constants.CONFIG_DEFAULT_KEY);
		
		logger.info("VARSQL_INSTALL_PATH : {}",VARSQL_INSTALL_PATH);
		logger.info("configuration system property : {}",configPropFile);
		
		File propFile = new File(VARSQL_INSTALL_PATH, CONFIG_FILE);
		
		if(null != configPropFile && !"".equals(configPropFile)){
			propFile = new File(configPropFile);
		}
		
		logger.info("config property file : {}",propFile);
		
		if ( ! propFile.canRead() ){
			
			logger.info("Can't open configuration file path: {}", propFile);
			
			throw new ConfigurationException( this.getClass().getName() + " - Can't open configuration file path: " + propFile);
		}
			
		FileInputStream jdf_fin = new FileInputStream(propFile);
		
		try{
			props.load(jdf_fin);
			
			setConfigProperty();
			jdf_fin.close();
		}finally{
			if(jdf_fin != null) jdf_fin.close();
		}
	}

	private void setConfigProperty() {
		useConnUID  = Boolean.parseBoolean(props.getProperty(USE_CONNID_KEY, "true"));
		passwordLen  = Integer.parseInt(props.getProperty(INIT_PASSWORD_SIZE, "8"));
		
		fileUploadPath= props.getProperty(FILE_UPLOAD_PATH, getInstallRoot() +File.separator + "upload");
		fileUploadSize  = Integer.parseInt(props.getProperty(FILE_UPLOAD_SIZE, "10485760"));
		fileUploadSizePerFile = Integer.parseInt(props.getProperty(FILE_UPLOAD_SIZEPERFILE, "5242880"));
		
		String initPasswordType = ""; 
				
		try{
			initPasswordType = props.getProperty(INIT_PASSWORD_TYPE,"");
			
			if(!"".equals(initPasswordType)){
				passwordType = PasswordType.valueOf(initPasswordType.toUpperCase());
			}
		}catch(Exception e){
			logger.error("password type config info :[ {} ] ", initPasswordType);
			logger.error("password error ",  e);
			passwordType = PasswordType.LOWERORNUMBER;
		}
	}
	
	public PasswordType passwordType (){
		return passwordType; 
	}
	
	public int passwordInitSize (){
		return passwordLen; 
	}
	/**
	 * 커넥션 초기화 
	 * @Method Name  : initConnection
	 * @Author : ytkim
	 * @Method desc :
	 * @History : 
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private void initConnection() throws ClassNotFoundException, Exception {
		File connectionFile = new File(VARSQL_INSTALL_PATH, CONNECTION_FILE);
		
		String connectionFileInfo = getConnectionFile();
		
		if(null != connectionFileInfo && !"".equals(connectionFileInfo)){
			connectionFile = new File(connectionFileInfo);
		}
		
		if(!connectionFile.exists()){
			logger.info("Can't open configuration file path key[com.varsql.connection.file] : {}", connectionFile);
			throw new ConfigurationException( this.getClass().getName() + " - Can't open configuration file path key[com.varsql.connection.file] : " + connectionFile);
		}
		
		parseXml(connectionFile);
		
		vConInfo.setConnid(ConnectionContext.DEFAULT_CONN_ID);
		
		logger.info("varsql Connection Info : {}" , vConInfo);
	}
	
	/**
	 * 커넥션 정보 파싱
	 * @Method Name  : parseXml
	 * @Author : ytkim
	 * @Method desc :
	 * @History : 
	 * @param xmlFile
	 */
	private void parseXml(File xmlFile) {
		
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList dataSource = doc.getElementsByTagName("dataSource");
			
			Element eElement = (Element)dataSource.item(0);

			vConInfo.setAliasName(eElement.getAttribute("name"));
			vConInfo.setType(getStringValue(eElement, "type","local"));
			vConInfo.setDriver(getStringValue(eElement,"driver","org.apache.derby.jdbc.EmbeddedDriver"));
			vConInfo.setUrl(getStringValue(eElement,"url","").replace("#resourePath#", getInstallRoot()));
			vConInfo.setUsername(getStringValue(eElement,"username",""));
			vConInfo.setPassword(getStringValue(eElement,"password",""));
			vConInfo.setMax_active(Integer.parseInt(getStringValue(eElement,"max_active","10")));
			vConInfo.setPool_opt(getStringValue(eElement,"pool_option",""));
			vConInfo.setConnection_opt(getStringValue(eElement,"connection_option",""));
			vConInfo.setTimebetweenevictionrunsmillis(Long.parseLong(getStringValue(eElement,"timebetweenevictionrunsmillis","")));
			vConInfo.setTest_while_idle(getStringValue(eElement,"test_while_idle",""));
			
		} catch (IOException io) {
			logger.error("CONNECTION_FILE IOException",io);
			throw new Error(io);
		} catch (XPathExpressionException jdomex) {
			logger.error("CONNECTION_FILE XPathExpressionException",jdomex);
			throw new Error(jdomex);
		} catch (ParserConfigurationException e) {
			logger.error("CONNECTION_FILE ParserConfigurationException",e);
			throw new Error(e);
		} catch (SAXException e) {
			logger.error("CONNECTION_FILE SAXException",e);
			throw new Error(e);
		}
	}

	private String getStringValue(Element rootNode, String nm ,String initVal) throws XPathExpressionException {
		Object e = xpath.evaluate("//property[@name='"+nm+"']", rootNode,XPathConstants.NODE); 
		return e==null?initVal:((Node)e).getAttributes().getNamedItem("value").getTextContent();
	}
	
	public Properties getProperties() {
		return props;
	}
	
	private String getSystemProperty(String key){
		return System.getProperty(key);
	}
	
	private static class ConfigurationHolder{
        private static final Configuration instance = new Configuration();
    }
	
	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
    }
	
	public String getType() {
		return props.getProperty(TYPE , "local");
	}
	
	public boolean useConnUID() {
		return useConnUID;
	}

	public String getConnectionFile() {
		return props.getProperty(Constants.DB_CONFIG_FILE);
	}

	public ConnectionInfo getVarsqlDB(){
		return vConInfo;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public long getFileUploadSize() {
		return fileUploadSize;
	}

	public int getFileUploadSizePerFile() {
		return fileUploadSizePerFile;
	}
}
