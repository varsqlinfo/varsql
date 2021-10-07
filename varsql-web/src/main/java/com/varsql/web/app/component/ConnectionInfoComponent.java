package com.varsql.web.app.component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.BeanType;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.ConnectionInfoConfig;
import com.varsql.core.connection.ConnectionInfoDao;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JdbcURLFormatParam;
import com.varsql.core.crypto.DBPasswordCryptionFactory;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.varsql.web.repository.user.FileInfoEntityRepository;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.utils.StringUtils;
import com.vartech.common.utils.VartechReflectionUtils;

@Component("connectionInfoDao")
@ConnectionInfoConfig(beanType = BeanType.SPRING, beanName = "connectionInfoDao", primary = true)
public class ConnectionInfoComponent implements ConnectionInfoDao {

	private final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	private DBConnectionEntityRepository dbConnectionEntityRepository;

	private FileInfoEntityRepository fileInfoEntityRepository;

	public ConnectionInfoComponent(DBConnectionEntityRepository dbConnectionEntityRepository, FileInfoEntityRepository fileInfoEntityRepository) {
		this.dbConnectionEntityRepository = dbConnectionEntityRepository;
		this.fileInfoEntityRepository = fileInfoEntityRepository;
	}

	@Override
	public ConnectionInfo getConnectionInfo(String connid) throws EncryptDecryptException {

		logger.debug("create connection info : {}", connid);

		ConnectionInfo connInfo = new ConnectionInfo();

		ConnectionInfoDTO dto = dbConnectionEntityRepository.findByConnInfo(connid);


		connInfo.setConnid(dto.getConnection().getVconnid());
		connInfo.setAliasName(dto.getConnection().getVname());
		connInfo.setType(dto.getProvider().getDbType().toLowerCase());
		connInfo.setDriver(dto.getProvider().getDriverClass());
		connInfo.setUsername(dto.getConnection().getVid());

		String pw = dto.getConnection().getVpw();
		connInfo.setPassword("");

		if (!StringUtils.isBlank(pw)) {
			connInfo.setPassword(DBPasswordCryptionFactory.getInstance().decrypt(pw));
		}

		connInfo.setPool_opt(dto.getConnection().getVpoolopt());
		connInfo.setConnection_opt(dto.getConnection().getVconnopt());
		connInfo.setMax_active(NumberUtils.toInt(dto.getConnection().getMaxActive()+"", 10));
		connInfo.setMin_idle(NumberUtils.toInt(dto.getConnection().getMinIdle()+"", 3));
		connInfo.setConnectionTimeOut(NumberUtils.toInt(dto.getConnection().getTimeout()+"", 18000));
		connInfo.setExportCount(NumberUtils.toInt(dto.getConnection().getExportcount()+"", 1000));

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

		connInfo.setValidation_query(validation_query);

		System.out.println(VartechReflectionUtils.reflectionToString(dto.getConnection()));
		System.out.println(VartechReflectionUtils.reflectionToString(dto.getProvider()));
		System.out.println(VartechReflectionUtils.reflectionToString(dto.getDriver()));

		List<FileInfoEntity> driverJarFiles= fileInfoEntityRepository.findByFileContId(dto.getProvider().getDriverProviderId());

		List<FileInfo> jarFileList = new ArrayList<>();
		for(FileInfoEntity fie : driverJarFiles) {
			FileInfo fi = new FileInfo();

			fi.setName(fie.getFileName());
			fi.setPath(Paths.get(VarsqlConstants.UPLOAD_PATH).toAbsolutePath().resolve(fie.getFilePath()).normalize().toAbsolutePath().toString());
			fi.setExt(fie.getFileExt());

			jarFileList.add(fi);
		}

		connInfo.setJdbcDriverList(jarFileList);

		return connInfo;
	}
}
