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
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionContext;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.exception.ConfigurationException;
import com.vartech.common.crypto.password.PasswordType;
import com.vartech.common.utils.IOUtils;
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
	
	private Properties props = new Properties();

	private ConnectionInfo vConInfo;
	private MailConfigBean mailConfigBean;

	private final String USE_CONNID_KEY = "varsql.connid.uuid.use";
	private final String PASSWORD_INIT_TYPE = "varsql.password.init.type";
	private final String PASSWORD_INIT_SIZE = "varsql.password.init.size";
	private final String FILE_UPLOAD_PATH = "file.upload.path";
	private final String FILE_UPLOAD_SIZE = "file.upload.size";
	private final String FILE_UPLOAD_SIZEPERFILE = "file.upload.sizeperfile";
	private final String FILE_UPLOAD_MAX_IN_MEMORY_SIZE = "file.upload.maxinmemorysize";
	private final String BACKUP_PATH = "backup.path";
	private final String BACKUP_EXPIRE_PATH = "backup.expire.day";
	private final String BACKUP_EXPIRE_CRON = "backup.expire.cron";

	private PasswordType passwordType;

	private int passwordLen;
	
	private String siteAddr;
	
	private String protocol;
	
	private String hostname;
	
	private String contextPath;
	
	private String connectiondaoPackage;
	
	private int port;

	private boolean useConnUID = true;
	
	private boolean scheduleEnable = true;

	private String fileUploadPath="";
	
	private String backupPath="";
	private String backupExpireCron="";
	
	private int backupExpireDay = 30;

	private long fileUploadSize=0;

	private long fileUploadSizePerFile=0;

	private int fileUploadMaxInMemorySize=0;
	
	private boolean isInit;
	
	private VarsqlConstants.PASSWORD_RESET_MODE passwordResetMode;
	
	private static class ConfigurationHolder{
        private static final Configuration instance = new Configuration();
    }

	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
    }

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
		File file = new File(VARSQL_INSTALL_PATH, VARSQL_PROPERTIES_FILE);

		logger.info("Varsql Resource Path : {}",VARSQL_INSTALL_PATH);
		logger.info("Configuration file : {}",file);
		
		if(!file.exists()) {
			isInit = false;
			try(InputStream is= ResourceUtils.getResource(ResourceUtils.CLASS_PREFIX+VARSQL_PROPERTIES_FILE).getInputStream();){
				if(!new File(file.getParent()).exists()) {
					new File(file.getParent()).mkdirs();
				}
				
				System.out.println("file.getParent() : "+ file.getParent());
				
				
				IOUtils.write(is, file);
			}catch(Exception e) {
				logger.error("properties init error : {}", e.getMessage(), e);
			}
		}else {
			isInit = true;
		}

		Resource configResource = getResourceFile(VARSQL_PROPERTIES_FILE);

		if (configResource ==null ){
			throw new ConfigurationException("Can't open configuration file : " + String.format("default path : %s/%s ", VARSQL_INSTALL_PATH, VARSQL_PROPERTIES_FILE));
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
		backupPath= props.getProperty(BACKUP_PATH, getInstallRoot() +File.separator + "backup");
		backupExpireDay= Integer.parseInt(props.getProperty(BACKUP_EXPIRE_PATH, "30"));
		
		backupExpireDay = backupExpireDay > 0 ? backupExpireDay * -1 : backupExpireDay; 
		backupExpireCron= props.getProperty(BACKUP_EXPIRE_CRON, "0 0 3 * * ?");
		fileUploadSize  = Long.parseLong(props.getProperty(FILE_UPLOAD_SIZE, "1048576000"));
		fileUploadSizePerFile = Long.parseLong(props.getProperty(FILE_UPLOAD_SIZEPERFILE, "1048576000"));
		fileUploadMaxInMemorySize = Integer.parseInt(props.getProperty(FILE_UPLOAD_MAX_IN_MEMORY_SIZE, "0"));
		
		// varsql server config
		protocol = props.getProperty("varsql.protocol", "http");
		hostname = props.getProperty("varsql.hostname", "localhost");
		port = Integer.parseInt(props.getProperty("varsql.port", "12345"));
		contextPath = props.getProperty("varsql.contextpath", "/vsql");
		connectiondaoPackage = props.getProperty("varsql.connectiondao.package", "com.varsql");
		
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
		logger.debug("backupPath : {}",backupPath);
		logger.debug("fileUpload uploadSize:{}, MaxInMemorySize : {}, sizePerFile: {}, ", fileUploadSize, fileUploadMaxInMemorySize, fileUploadSizePerFile);

		passwordType = PasswordType.getType(props.getProperty(PASSWORD_INIT_TYPE,""));
		
		scheduleEnable = "true".equals(props.getProperty("schedule.enable","true"));
	
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

		logger.info("connection default config file : {}",CONNECTION_XML_FILE);

		if(null != connectionFileInfo && !"".equals(connectionFileInfo)){
			logger.info("connection config read file path : {}", connectionFileInfo);
			connectionResource = ResourceUtils.getResource(connectionFileInfo);
		}else {
			File file = new File(VARSQL_INSTALL_PATH, CONNECTION_XML_FILE);
			
			if(!file.exists()) {
				try(InputStream is= ResourceUtils.getResource(ResourceUtils.CLASS_PREFIX+CONNECTION_XML_FILE).getInputStream();){
					IOUtils.write(is, file);
				}catch(Exception e) {
					logger.error("db connection file create error : {}", e.getMessage(), e);
				}
			}
			
			logger.info("connection config read file path : {}", file.getAbsolutePath());
			
			connectionResource = ResourceUtils.getResource(file.getPath());
		}

		if(connectionResource==null){
			throw new ConfigurationException("Can't open connection configuration file : " + String.format("default path : %s/%s , config path : %s", VARSQL_INSTALL_PATH, CONNECTION_XML_FILE ,connectionFileInfo));
		}

		parseXml(connectionResource);

		logger.info("varsql Connection Info : {}" , this.vConInfo);
		logger.info("varsql.timezone info : {}" , getTimeZoneId());
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
			
			String type = jsonInfo.get("type").asText("h2"); 
			this.vConInfo = ConnectionInfo.builder()
				.connid(ConnectionContext.DEFAULT_CONN_ID)
				.aliasName(jsonInfo.get("name").asText(""))
				.type(type)
				
				.url(jsonInfo.get("url").asText("jdbc:h2:file:#resourePath#/varsql;MV_STORE=FALSE;CACHE_SIZE=131072;").replace("#resourePath#", getInstallRoot()))
				.username(jsonInfo.get("username").asText(""))
				.password(jsonInfo.get("password").asText(""))
				
				.initialSize(jsonInfo.get("initial_size")==null ? 5 : jsonInfo.get("initial_size").asInt(5))
				.minIdle(jsonInfo.get("min_idle") ==null ? 3 : jsonInfo.get("min_idle").asInt(3))
				.maxIdle(jsonInfo.get("max_idle") == null ? 10 : jsonInfo.get("max_idle").asInt(10))
				.maxActive(jsonInfo.get("max_active") == null ? 10 : jsonInfo.get("max_active").asInt(10))
				.connectionOptions(jsonInfo.get("connection_option").asText(""))
				.timebetweenevictionrunsmillis(jsonInfo.get("timebetweenevictionrunsmillis").asLong())
				.testWhileIdle(Boolean.parseBoolean(jsonInfo.get("test_while_idle").asText()))
				.validationQuery(ValidationProperty.getInstance().validationQuery(type))
				.jdbcDriverInfo(JDBCDriverInfo.builder()
					.providerId("baseProvider")
					.driverId("base")
					.driverClass(jsonInfo.get("driver").asText("org.h2.Driver"))
					.build()
				)
			.build();
			
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

	public String getDbType() {
		return this.vConInfo.getType();
	}

	public boolean useConnUID() {
		return useConnUID;
	}

	public String getConnectionFile() {
		return props.getProperty(Constants.DB_CONFIG_FILE);
	}

	public ConnectionInfo getVarsqlDB(){
		return this.vConInfo;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}
	
	public String getBackupPath() {
		return backupPath;
	}
	
	public int backupExpireDay() {
		return backupExpireDay;
	}
	
	public String getBackupExpireCron() {
		return backupExpireCron;
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

	public boolean isScheduleEnable() {
		return scheduleEnable;
	}
	
	public Resource getQuartzConfig() {
		return getResourceFile(VARSQL_QUARTZ_PROPERTIES_FILE);
	}
	
	public Resource getHibernateConfig() {
		return getResourceFile(VARSQL_HIBERNATE_PROPERTIES_FILE);
	}
	
	private Resource getResourceFile(String filePath) {
		return getResourceFile(filePath, false);
	}
	
	private Resource getResourceFile(String filePath, boolean createFlag) {
		File file = new File(VARSQL_INSTALL_PATH, filePath);
		if(file.exists()) {
			return ResourceUtils.getResource(file.getPath());
		}else {
			return ResourceUtils.getResource(filePath);
		}
	}

	public String getConnectiondaoPackage() {
		return connectiondaoPackage;
	}
	
	public String getTimeZoneId() {
		return getProperties().getProperty("varsql.timezone","");
	}

	public boolean isInit() {
		return isInit;
	}

}
