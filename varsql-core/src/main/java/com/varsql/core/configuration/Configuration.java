package com.varsql.core.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.connection.ConnectionContext;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.exception.ConfigurationException;
import com.vartech.common.crypto.password.PasswordType;
import com.vartech.common.utils.VartechUtils;


/**
 *
 * @FileName : Configuration.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 28.
 * @프로그램설명: varsql 관련 설정값 읽는 클래스.
 * @변경이력	:
 */
public class Configuration extends AbstractConfiguration{
	private final Logger logger = LoggerFactory.getLogger(Configuration.class);

	final String VARSQL_INSTALL_PATH = getInstallRoot();
	final String CONFIG_FILE= "config/varsqlConfig.properties";
	final String CONNECTION_CONFIG_FILE= "config/varsqlConnectionConfig.xml";

	private Properties props = new Properties();

	private ConnectionInfo vConInfo = new ConnectionInfo();

	private final String TYPE = "varsql.type";

	private final String USE_CONNID_KEY = "varsql.connid.uuid.use";
	private final String INIT_PASSWORD_TYPE = "varsql.init.password.type";
	private final String INIT_PASSWORD_SIZE = "varsql.init.password.size";
	private final String FILE_UPLOAD_PATH = "file.upload.path";
	private final String FILE_UPLOAD_SIZE = "file.upload.size";
	private final String FILE_UPLOAD_SIZEPERFILE = "file.upload.sizeperfile";
	private final String FILE_UPLOAD_MAX_IN_MEMORY_SIZE = "file.upload.maxinmemorysize";

	private PasswordType passwordType;

	private int passwordLen;

	private boolean useConnUID = true;

	private String fileUploadPath="";

	private int fileUploadSize=0;

	private int fileUploadSizePerFile=0;

	private int fileUploadMaxInMemorySize=0;

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
		logger.info("configuration filename : {}",CONFIG_FILE);

		Resource configResource;

		if(null != configPropFile && !"".equals(configPropFile)){
			configResource = ResourceUtils.getResource(configPropFile);
		}else {
			File file = new File(VARSQL_INSTALL_PATH, CONFIG_FILE);
			if(file.exists()) {
				configResource = ResourceUtils.getResource(file.getPath());
			}else {
				configResource = ResourceUtils.getResource(CONFIG_FILE);
			}
		}

		if (configResource ==null ){
			throw new ConfigurationException("Can't open configuration file : " + String.format("default path : %s/%s , config path : %s", VARSQL_INSTALL_PATH, CONFIG_FILE ,configPropFile));
		}
		
		try(InputStream is = configResource.getInputStream()){
			props.load(is);
		}catch(IOException e) {
			throw new ConfigurationException(e);
		}

		setConfigProperty();
	}

	private void setConfigProperty() {
		useConnUID  = Boolean.parseBoolean(props.getProperty(USE_CONNID_KEY, "true"));
		passwordLen  = Integer.parseInt(props.getProperty(INIT_PASSWORD_SIZE, "8"));
		
		fileUploadPath= props.getProperty(FILE_UPLOAD_PATH, getInstallRoot() +File.separator + "upload");
		fileUploadSize  = Integer.parseInt(props.getProperty(FILE_UPLOAD_SIZE, "1048576000"));
		fileUploadSizePerFile = Integer.parseInt(props.getProperty(FILE_UPLOAD_SIZEPERFILE, "31457280"));
		fileUploadMaxInMemorySize = Integer.parseInt(props.getProperty(FILE_UPLOAD_MAX_IN_MEMORY_SIZE, "0"));
		
		logger.debug("passwordLen : {}",passwordLen);
		logger.debug("fileUploadPath : {}",fileUploadPath);
		logger.debug("fileUploadMaxInMemorySize : {}",fileUploadMaxInMemorySize);
		logger.debug("fileUploadSizePerFile : {}",fileUploadSizePerFile);
		logger.debug("fileUploadSize : {}",fileUploadSize);

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
		String connectionFileInfo = getConnectionFile();
		Resource connectionResource;

		logger.info("connection default config file : {}",CONNECTION_CONFIG_FILE);

		if(null != connectionFileInfo && !"".equals(connectionFileInfo)){
			logger.info("connection config read file path : {}", connectionFileInfo);
			connectionResource = ResourceUtils.getResource(connectionFileInfo);
		}else {
			File file = new File(VARSQL_INSTALL_PATH, CONNECTION_CONFIG_FILE);
			if(file.exists()) {
				connectionResource = ResourceUtils.getResource(file.getPath());
			}else {
				connectionResource = ResourceUtils.getResource(CONNECTION_CONFIG_FILE);
			}
		}

		if(connectionResource==null){
			throw new ConfigurationException("Can't open connection configuration file : " + String.format("default path : %s/%s , config path : %s", VARSQL_INSTALL_PATH, CONNECTION_CONFIG_FILE ,connectionFileInfo));
		}

		parseXml(connectionResource);

		logger.info("varsql Connection Info : {}" , vConInfo);

		// 기본 디비 connection pool 생성. connection 정보를 얻기위한 pool
		ConnectionInfo baseConInfo = new ConnectionInfo();
		BeanUtils.copyProperties(baseConInfo, vConInfo);

		baseConInfo.setMax_active(2);
		baseConInfo.setMin_idle(1);

		ConnectionFactory.getInstance().createPool(baseConInfo);
	}

	/**
	 * 커넥션 정보 파싱
	 * @Method Name  : parseXml
	 * @Author : ytkim
	 * @Method desc :
	 * @History :
	 * @param connectionResource
	 */
	private void parseXml(Resource connectionResource) {

		try {
			JsonNode jsonInfo =VartechUtils.xmlToJsonNode(ResourceUtils.getResourceString(connectionResource, getCharset()));

			vConInfo.setConnid(ConnectionContext.DEFAULT_CONN_ID);
			vConInfo.setAliasName(jsonInfo.get("name").asText(""));
			vConInfo.setType(jsonInfo.get("name").asText("local"));
			vConInfo.setDriver(jsonInfo.get("driver").asText("org.h2.Driver"));
			vConInfo.setUrl(jsonInfo.get("url").asText("org.h2.Driver").replace("#resourePath#", getInstallRoot()));
			vConInfo.setUsername(jsonInfo.get("username").asText(""));
			vConInfo.setPassword(jsonInfo.get("password").asText(""));
			vConInfo.setMax_active(jsonInfo.get("max_active").asInt(10));
			vConInfo.setPool_opt(jsonInfo.get("pool_option").asText(""));
			vConInfo.setConnection_opt(jsonInfo.get("connection_option").asText(""));
			vConInfo.setTimebetweenevictionrunsmillis(jsonInfo.get("timebetweenevictionrunsmillis").asLong());
			vConInfo.setTest_while_idle(jsonInfo.get("test_while_idle").asText());

		} catch (IOException io) {
			logger.error("CONNECTION_FILE IOException",io);
			throw new Error(io);
		}
	}

	private Properties getProperties() {
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

	public String getDbType() {
		return props.getProperty(TYPE , "h2");
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
	
	public int getFileUploadMaxInMemorySize() {
		return fileUploadMaxInMemorySize;
	}

	public String getCharset() {
		return getProperties().getProperty("varsql.default.charset","utf-8");
	}

	public String getDbPwSecurityKey() {
		return getProperties().getProperty("varsql.db.pw.secret.key","MTZkNzMwM2QtMDQ4NS0zOTlhLWEyZmMtODAwNTg0NDY0NzZk");
	}

	public String getDbPWCryptoType() {
		return getProperties().getProperty("varsql.db.pw.crpyto","aes");
	}

	public String getDbPWCustomClass() {
		return getProperties().getProperty("varsql.db.pw.custom.class","");
	}
}
