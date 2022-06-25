package com.varsql.web.app.component;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.common.constants.PathType;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.BeanType;
import com.varsql.core.connection.ConnectionInfoConfig;
import com.varsql.core.connection.ConnectionInfoDao;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.connection.beans.JdbcURLFormatParam;
import com.varsql.core.crypto.DBPasswordCryptionFactory;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.db.DBTypeDriverFileEntityRepository;
import com.varsql.web.util.FileServiceUtils;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.utils.StringUtils;

@Component("connectionInfoDao")
@ConnectionInfoConfig(beanType = BeanType.SPRING, beanName = "connectionInfoDao", primary = true)
public class ConnectionInfoComponent implements ConnectionInfoDao {

	private final Logger logger = LoggerFactory.getLogger(ConnectionInfoComponent.class);

	private DBConnectionEntityRepository dbConnectionEntityRepository;

	private DBTypeDriverFileEntityRepository dbTypeDriverFileEntityRepository;

	public ConnectionInfoComponent(DBConnectionEntityRepository dbConnectionEntityRepository, DBTypeDriverFileEntityRepository dbTypeDriverFileEntityRepository) {
		this.dbConnectionEntityRepository = dbConnectionEntityRepository;
		this.dbTypeDriverFileEntityRepository = dbTypeDriverFileEntityRepository;
	}

	@Override
	public ConnectionInfo getConnectionInfo(String connid) throws EncryptDecryptException {

		logger.debug("create connection info : {}", connid);

		ConnectionInfo connInfo = new ConnectionInfo();

		ConnectionInfoDTO dto = dbConnectionEntityRepository.findByConnInfo(connid);

		connInfo.setConnid(dto.getConnection().getVconnid());
		connInfo.setAliasName(dto.getConnection().getVname());
		connInfo.setType(dto.getProvider().getDbType().toLowerCase());
		connInfo.setUsername(dto.getConnection().getVid());

		String pw = dto.getConnection().getVpw();
		connInfo.setPassword("");

		if (!StringUtils.isBlank(pw)) {
			connInfo.setPassword(DBPasswordCryptionFactory.getInstance().decrypt(pw));
		}

		connInfo.setPoolOptions(dto.getConnection().getVpoolopt());
		connInfo.setConnectionOptions(dto.getConnection().getVconnopt());
		connInfo.setMaxActive(NumberUtils.toInt(dto.getConnection().getMaxActive()+"", 10));
		connInfo.setMinIdle(NumberUtils.toInt(dto.getConnection().getMinIdle()+"", 3));
		connInfo.setConnectionTimeOut(NumberUtils.toInt(dto.getConnection().getTimeout()+"", 18000));
		connInfo.setExportCount(NumberUtils.toInt(dto.getConnection().getExportcount()+"", 1000));
		connInfo.setTestWhileIdle("Y".equals(dto.getConnection().getTestWhileIdle()));
		
		String defaultDriverValidationQuery = ValidationProperty.getInstance().validationQuery(connInfo.getType());

		String urlDirectYn = dto.getConnection().getUrlDirectYn();
		if ("Y".equals(urlDirectYn)) {
			connInfo.setUrl(dto.getConnection().getVurl());
		} else {
			connInfo.setUrl(VarsqlJdbcUtil.getJdbcUrl(dto.getDriver().getUrlFormat(), JdbcURLFormatParam.builder()
					.serverIp(dto.getConnection().getVserverip())
					.port(Integer.parseInt(dto.getConnection().getVport()+""))
					.databaseName(dto.getConnection().getVdatabasename())
					.build()));
		}

		defaultDriverValidationQuery = StringUtils.isBlank(dto.getDriver().getValidationQuery()) ? defaultDriverValidationQuery : dto.getDriver().getValidationQuery();

		String validation_query = dto.getProvider().getValidationQuery();

		validation_query = StringUtils.isBlank(validation_query) ? defaultDriverValidationQuery : validation_query;

		connInfo.setValidationQuery(validation_query);

		List<FileInfo> driverFileInfos;
		
		if(PathType.PATH.equals(PathType.getPathType(dto.getProvider().getPathType()))){
			try {
				driverFileInfos = FileServiceUtils.getFileInfos(dto.getProvider().getDriverPath().split(";"));
			} catch (IOException e) {
				logger.error("driver load fail : "+ dto.getProvider().getDriverPath());
				driverFileInfos = null; 
			}
		}else {
			driverFileInfos = FileServiceUtils.getFileInfos(dbTypeDriverFileEntityRepository.findByFileContId(dto.getProvider().getDriverProviderId()));
		}

		JDBCDriverInfo jdbcDriverInfo = new JDBCDriverInfo(dto.getDriver().getDriverId(), dto.getDriver().getDbdriver());
	    jdbcDriverInfo.setDriverFiles(driverFileInfos);
	    
	    connInfo.setJdbcDriverInfo(jdbcDriverInfo);

		return connInfo;
	}
}
