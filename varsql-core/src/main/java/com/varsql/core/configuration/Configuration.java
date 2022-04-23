package com.varsql.core.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.configuration.beans.MailConfigBean;
import com.varsql.core.connection.ConnectionContext;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
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
	private MailConfigBean mailConfigBean;

	private final String TYPE = "varsql.type";

	private final String USE_CONNID_KEY = "varsql.connid.uuid.use";
	private final String PASSWORD_INIT_TYPE = "varsql.password.init.type";
	private final String PASSWORD_INIT_SIZE = "varsql.password.init.size";
	private final String FILE_UPLOAD_PATH = "file.upload.path";
	private final String FILE_UPLOAD_SIZE = "file.upload.size";
	private final String FILE_UPLOAD_SIZEPERFILE = "file.upload.sizeperfile";
	private final String FILE_UPLOAD_MAX_IN_MEMORY_SIZE = "file.upload.maxinmemorysize";

	private PasswordType passwordType;

	private int passwordLen;
	
	private String siteAddr;
	
	private String protocol;
	
	private String hostname;
	
	private String contextPath;
	
	private int port;

	private boolean useConnUID = true;

	private String fileUploadPath="";

	private long fileUploadSize=0;

	private long fileUploadSizePerFile=0;

	private int fileUploadMaxInMemorySize=0;
	
	private VarsqlConstants.PASSWORD_RESET_MODE passwordResetMode;

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
		
		mailConfigBean = MailConfigBean.getMailConfigBean(props);
	}

	private void setConfigProperty() {
		useConnUID  = Boolean.parseBoolean(props.getProperty(USE_CONNID_KEY, "true"));
		// password reset
		passwordLen  = Integer.parseInt(props.getProperty(PASSWORD_INIT_SIZE, "8"));
		passwordResetMode = VarsqlConstants.PASSWORD_RESET_MODE.getMode(props.getProperty("varsql.password.reset.mode", "manager"));

		// file upload config
		fileUploadPath= props.getProperty(FILE_UPLOAD_PATH, getInstallRoot() +File.separator + "upload");
		fileUploadSize  = Long.parseLong(props.getProperty(FILE_UPLOAD_SIZE, "1048576000"));
		fileUploadSizePerFile = Long.parseLong(props.getProperty(FILE_UPLOAD_SIZEPERFILE, "1048576000"));
		fileUploadMaxInMemorySize = Integer.parseInt(props.getProperty(FILE_UPLOAD_MAX_IN_MEMORY_SIZE, "0"));
		
		// varsql server config
		protocol = props.getProperty("varsql.protocol", "http");
		hostname = props.getProperty("varsql.hostname", "localhost");
		port = Integer.parseInt(props.getProperty("varsql.port", "12312"));
		contextPath = props.getProperty("varsql.contextpath", "/vsql");
		
		siteAddr = protocol;
		
		if("localhost".equals(hostname)) {
			
			try {
				hostname = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		siteAddr = String.format("%s://%s:%d%s", protocol, hostname, port, contextPath);
		
		logger.debug("siteAddr : {}", siteAddr);
		logger.debug("passwordLen : {}",passwordLen);
		logger.debug("fileUploadPath : {}",fileUploadPath);
		logger.debug("fileUploadMaxInMemorySize : {}",fileUploadMaxInMemorySize);
		logger.debug("fileUploadSizePerFile : {}",fileUploadSizePerFile);
		logger.debug("fileUploadSize : {}",fileUploadSize);

		String initPasswordType = props.getProperty(PASSWORD_INIT_TYPE,"");

		passwordType = "".equals(initPasswordType) ? PasswordType.LOWERORNUMBER : PasswordType.valueOf(initPasswordType.toUpperCase());
	
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
			
			vConInfo.setUrl(jsonInfo.get("url").asText("jdbc:h2:file:#resourePath#/varsql;MV_STORE=FALSE;CACHE_SIZE=131072;").replace("#resourePath#", getInstallRoot()));
			vConInfo.setUsername(jsonInfo.get("username").asText(""));
			vConInfo.setPassword(jsonInfo.get("password").asText(""));
			vConInfo.setMaxActive(jsonInfo.get("max_active").asInt(10));
			vConInfo.setPoolOptions(jsonInfo.get("pool_option").asText(""));
			vConInfo.setConnectionOptions(jsonInfo.get("connection_option").asText(""));
			vConInfo.setTimebetweenevictionrunsmillis(jsonInfo.get("timebetweenevictionrunsmillis").asLong());
			vConInfo.setTestWhileIdle(Boolean.parseBoolean(jsonInfo.get("test_while_idle").asText()));
			
			JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo("base", jsonInfo.get("driver").asText("org.h2.Driver"));  
		    this.vConInfo.setJdbcDriverInfo(jdbcDriverInfo);

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

	public long getFileUploadSizePerFile() {
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

	public MailConfigBean getMailConfigBean() {
		return mailConfigBean;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getSiteAddr() {
		return siteAddr;
	}

	public String getContextPath() {
		return contextPath;
	}

	public VarsqlConstants.PASSWORD_RESET_MODE getPasswordResetMode() {
		return passwordResetMode;
	}
}
