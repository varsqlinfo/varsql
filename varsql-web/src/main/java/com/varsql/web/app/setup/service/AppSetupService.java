package com.varsql.web.app.setup.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.varsql.core.changeset.DatabaseChangeExecutor;
import com.varsql.core.common.TemplateFactory;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.ResourceUtils;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.ConnectionContext;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.web.dto.setup.AdminVO;
import com.varsql.web.dto.setup.DatabaseVO;
import com.varsql.web.dto.setup.SetupConfigDTO;
import com.varsql.web.util.PasswordUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.crypto.password.PasswordType;
import com.vartech.common.crypto.password.PasswordUtil;
import com.vartech.common.io.RESOURCE_TYPE;
import com.vartech.common.io.Resource;
import com.vartech.common.prop.PropertyResource;
import com.vartech.common.utils.IOUtils;
import com.vartech.common.utils.StringUtils;

@Service
public class AppSetupService{
	private final static Logger logger = LoggerFactory.getLogger(AppSetupService.class);


	public ResponseResult install(SetupConfigDTO setupConfigDTO) {
		
		logger.info("install start ");
		logger.info("config info :{} ", setupConfigDTO);
		
		ResponseResult resultObject = new ResponseResult();
		
		List<FieldError> validateResult;
		try {
			validateResult = VarsqlUtils.validationCheck(setupConfigDTO, "userInfo", "appInfo","dbInfo");
			
			if(validateResult != null && validateResult.size() > 0) {
		    	
		    	resultObject.setResultCode(RequestResultCode.DATA_NOT_VALID);
		    	
		    	FieldError errorInfo = validateResult.get(0);
				resultObject.setItemOne(errorInfo.getField());
				resultObject.setMessage(VarsqlUtils.errorMessage(errorInfo));
		    	
				return resultObject; 
			}
			
		} catch (Exception e) {
			resultObject.setMessage(e.getMessage());
			return resultObject;
		}
		
		try {
			installAppProp(setupConfigDTO);
			installDbInfo(setupConfigDTO);
			createDbInfo(setupConfigDTO);
		} catch (Exception e) {
			resultObject.setResultCode(RequestResultCode.ERROR);
			resultObject.setMessage(e.getMessage());
			
			e.printStackTrace();
			
			return resultObject;
		}
		
		resultObject.setItemOne("success");
		return resultObject;
	}


	/**
	 * db table & 초기 data 생성.
	 * @param setupConfigDTO
	 * @throws Exception 
	 * @throws IOException 
	 */
	private void createDbInfo(SetupConfigDTO setupConfigDTO) throws IOException, Exception {
		DatabaseVO dbInfo = setupConfigDTO.getDbInfo();
		
		ConnectionInfo connectionInfo= ConnectionInfo.builder()
			.connid(ConnectionContext.DEFAULT_CONN_ID)
			.aliasName(ConnectionContext.DEFAULT_ALIAS)
			.type(dbInfo.getType())
			.url(dbInfo.getUrl().replace("#resourePath#", Configuration.getInstance().getInstallRoot()))
			.username(dbInfo.getUsername())
			.password(dbInfo.getPw())
		.build();
		
		DatabaseChangeExecutor dce = new DatabaseChangeExecutor(connectionInfo);
		dce.changeApply();
		
		createManagerInfo(dce, setupConfigDTO);
	}
	
	/**
	 * 관리자 계정 생성
	 * 
	 * @param dce
	 * @param setupConfigDTO
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void createManagerInfo(DatabaseChangeExecutor dce, SetupConfigDTO setupConfigDTO) throws ClassNotFoundException, SQLException {
		AdminVO userInfo = setupConfigDTO.getUserInfo();
		
		dce.executeSQL("insert into VTUSER ( viewid, uid, upw, uname, uemail, user_role, reg_id, upd_id, accept_yn)\n"
				+ " values('0000001', ?, ?, ?, ?, 'ADMIN', 'varsqladmin', 'varsqladmin', 'Y');", 
				userInfo.getId(), PasswordUtils.encode(userInfo.getPw()), userInfo.getName(), userInfo.getEmail());
		
	}

	/**
	 * db 설정 정보 생성
	 * 
	 * @param setupConfigDTO 
	 * @throws IOException
	 */
	private void installDbInfo(SetupConfigDTO setupConfigDTO) throws IOException {
		DatabaseVO dbInfo = setupConfigDTO.getDbInfo();
		
		DBVenderType dbType =DBVenderType.getDBType(dbInfo.getType());
		
		// h2인 경우 기본 정보로 생성
		if(DBVenderType.H2.equals(dbType)) {
			dbInfo.setUrl(Configuration.DEFAULT_JDBC_URL);
			dbInfo.setDriverClass(Configuration.DEFAULT_JDBC_DRIVER);
			dbInfo.setUsername("sa");
			dbInfo.setPw("sa");
		}else {
			dbInfo.setDriverClass(DBVenderType.getDBType(dbInfo.getType()).getDriverClass());
			
			try {
				JdbcUtils.connectionTest( dbType
				    		, dbInfo.getDriverClass()
				    		, dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPw() , 30, 20);
			} catch (Exception e) {
				throw new VarsqlRuntimeException(VarsqlAppCode.EC_DB_CONNECTION, "db connection error "+ e.getMessage(), e);
			}
		}
		
		String configPath = Configuration.getInstance().appDbConfigFilePath();
		File file = new File(Configuration.getInstance().getInstallRoot(), configPath);
		if(!file.exists()) {
			String xmlTemplate = ResourceUtils.getResourceString(RESOURCE_TYPE.CLASSPATH.getPath(configPath));
			
			File dbFile = ResourceUtils.getInstallPathFile(configPath);
			logger.info("db file path : {}", dbFile);
			
			if(!dbFile.getParentFile().exists()) {
				dbFile.getParentFile().mkdirs();
			}
			
			String dbXmlInfo = TemplateFactory.getInstance().render(xmlTemplate, dbInfo);
			
			IOUtils.write(dbXmlInfo, new FileOutputStream(dbFile), VarsqlConstants.CHAR_SET);
		}
	}

	/**
	 * application 설정 정보 생성
	 * 
	 * @param setupConfigDTO
	 * @throws IOException
	 */
	private void installAppProp(SetupConfigDTO setupConfigDTO) throws IOException {
		
		Resource resource = null;
		boolean isExsits = false; 
		String configPath = Configuration.getInstance().appConfigFilePath();
		File file = new File(Configuration.getInstance().getInstallRoot(), configPath);
		if(file.exists()) {
			isExsits = true; 
			resource = ResourceUtils.getResource(RESOURCE_TYPE.FILE.getPath(file.getPath()));
		}else {
			resource = ResourceUtils.getResource(RESOURCE_TYPE.CLASSPATH.getPath(configPath));
		}
			
		try(InputStream is = resource.getInputStream();){
			PropertyResource props = new PropertyResource(is);

			props.replaceProperty("varsql.default.charset", StringUtils.isBlank(setupConfigDTO.getAppInfo().getCharset())? VarsqlConstants.CHAR_SET :setupConfigDTO.getAppInfo().getCharset());
			props.replaceProperty("backup.expire.day", setupConfigDTO.getAppInfo().getFileRetentionPeriod()+"");
			
			if(!isExsits) {
				props.replaceProperty("varsql.db.pw.secret.key", PasswordUtil.createPassword(PasswordType.MULTIPLE, 45));
			}
			
			File installFile = ResourceUtils.getInstallPathFile(configPath);
			logger.info("install file path : {}", installFile);
			
			if(!installFile.getParentFile().exists()) {
				installFile.getParentFile().mkdirs();
			}
			
			props.store(new FileOutputStream(installFile), "");
		}
		
	}

}
