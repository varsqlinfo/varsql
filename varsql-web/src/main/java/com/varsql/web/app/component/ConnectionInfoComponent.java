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
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.meta.DBVersionInfo;
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

		ConnectionInfoDTO dto = dbConnectionEntityRepository.findByConnInfo(connid);

		String type = dto.getProvider().getDbType().toLowerCase(); 
		String pw = dto.getConnection().getVpw();
		String validation_query = dto.getProvider().getValidationQuery();
		String version =dto.getConnection().getVdbversion(); 
		
		List<DBVersionInfo> versionList = MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(type)).getVenderVersionInfo();
		
		DBVersionInfo versionInfo = versionList.stream().filter(item->item.getVersion().equals(version)).findFirst().orElse(MetaControlFactory.getDbInstanceFactory(DBVenderType.getDBType(type)).getDefaultVenderVersion());
		
		ConnectionInfo.ConnectionInfoBuilder builder = ConnectionInfo.builder()
			.connid(dto.getConnection().getVconnid())
			.aliasName(dto.getConnection().getVname())
			.type(type)
			.version(versionInfo)
			.username(dto.getConnection().getVid())
			.useColumnLabel(dto.getConnection().getUseColumnLabel())
			.connectionOptions(dto.getConnection().getVconnopt())
			.maxActive(NumberUtils.toInt(dto.getConnection().getMaxActive()+"", 10))
			.minIdle(NumberUtils.toInt(dto.getConnection().getMinIdle()+"", 5))
			.connectionTimeOut(NumberUtils.toInt(dto.getConnection().getTimeout()+"", 18000))
			.exportCount(NumberUtils.toInt(dto.getConnection().getExportcount()+"", 1000))
			.testWhileIdle("Y".equals(dto.getConnection().getTestWhileIdle()))
			.enableConnectionPool(!"N".equals(dto.getConnection().getEnableConnectionPool()))
			.validationQuery(StringUtils.isBlank(validation_query) ? ValidationProperty.getInstance().validationQuery(type) : validation_query)
			.password(StringUtils.isBlank(pw)?"":DBPasswordCryptionFactory.getInstance().decrypt(pw));
			
		String urlDirectYn = dto.getConnection().getUrlDirectYn();
		
		if ("Y".equals(urlDirectYn)) {
			builder.url(dto.getConnection().getVurl());
		} else {
			builder.url(VarsqlJdbcUtil.getJdbcUrl(dto.getDriver().getUrlFormat(), JdbcURLFormatParam.builder()
					.serverIp(dto.getConnection().getVserverip())
					.port(Integer.parseInt(dto.getConnection().getVport()+""))
					.databaseName(dto.getConnection().getVdatabasename())
					.build()));
		}
		
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
		
	    builder.jdbcDriverInfo(JDBCDriverInfo.builder()
	    	.providerId(dto.getProvider().getDriverProviderId())
    		.driverId(dto.getDriver().getDriverId())
    		.driverClass(dto.getDriver().getDbdriver())
    		.driverFiles(driverFileInfos)
    		.build());

		return builder.build();
	}
}
